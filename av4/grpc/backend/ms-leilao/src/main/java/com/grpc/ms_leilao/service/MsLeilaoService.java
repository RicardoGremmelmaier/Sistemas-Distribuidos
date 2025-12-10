package com.grpc.ms_leilao.service;

import com.leilao.common.grpc.notificacoes.EventoNotificacao;
import com.leilao.common.grpc.notificacoes.NotificacaoServiceGrpc;
import com.leilao.common.grpc.lance.*;
import com.leilao.common.grpc.pagamento.PagamentoRequestProto;
import com.leilao.common.grpc.pagamento.PagamentoResponseProto;
import com.leilao.common.grpc.pagamento.PagamentoServiceGrpc;
import com.grpc.ms_leilao.model.Leilao;
import com.grpc.ms_leilao.model.LeilaoStatus;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.Locale;

@Service
public class MsLeilaoService {

    private final Map<Integer, Leilao> leiloes = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    private final SchedulerService schedulerService;

    // --- Clientes gRPC ---
    @GrpcClient("lance-client")
    private LanceServiceGrpc.LanceServiceBlockingStub lanceStub;

    @GrpcClient("pagamento-client")
    private PagamentoServiceGrpc.PagamentoServiceBlockingStub pagamentoStub;

    @GrpcClient("gateway-client")
    private NotificacaoServiceGrpc.NotificacaoServiceBlockingStub gatewayStub;

    public MsLeilaoService(@Lazy SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    public Leilao criarLeilao(Leilao input) {
        Integer id = (input.getId() == null) ? idGenerator.getAndIncrement() : input.getId();
        input.setId(id);
        input.setStatus(LeilaoStatus.PENDENTE);

        leiloes.put(id, input);

        LocalDateTime now = LocalDateTime.now();
        if (input.getDataInicio() != null && !input.getDataInicio().isAfter(now)) {
            ativarLeilao(input);
        } else if (input.getDataInicio() != null) {
            schedulerService.agendarInicio(input.getId(), input.getDataInicio());
        }
        
        if (input.getDataFim() != null) {
            schedulerService.agendarFim(input.getId(), input.getDataFim());
        }

        System.out.println("Leilão criado: " + id);
        return input;
    }

    public void ativarLeilao(Leilao leilao) {
        if (leilao.getStatus() == LeilaoStatus.PENDENTE) {
            leilao.setStatus(LeilaoStatus.ATIVO);
            
            // Avisa o MS Lance para liberar lances
            try {
                lanceStub.iniciarLeilao(LeilaoDados.newBuilder()
                        .setId(leilao.getId())
                        .setStatus("ATIVO")
                        .build());
            } catch (Exception e) {
                System.err.println("Erro ao chamar MS Lance: " + e.getMessage());
            }

            // Notifica o Gateway (SSE)
            notificarGateway("leilao_iniciado", leilao.getId(), 
                String.format("{\"leilaoId\": %d, \"status\": \"ATIVO\", \"nome\": \"%s\"}", 
                leilao.getId(), leilao.getNomeDoProduto()));
        }
    }

    public void finalizarLeilao(Leilao leilao) {
        if (leilao.getStatus() != LeilaoStatus.FINALIZADO) {
            leilao.setStatus(LeilaoStatus.FINALIZADO);
            System.out.println("Finalizando leilão " + leilao.getId());

            // Avisa o MS Lance para travar novos lances
            try {
                lanceStub.finalizarLeilao(LeilaoIdRequest.newBuilder().setId(leilao.getId()).build());
                
                // Obtem vencedor
                VencedorResponse vencedor = lanceStub.obterVencedor(
                        LeilaoIdRequest.newBuilder().setId(leilao.getId()).build());

                if (vencedor.getTemVencedor()) {
                    leilao.setIdVencedor(vencedor.getClienteId());
                    
                    // Notifica Gateway sobre o vencedor
                    notificarGateway("leilao_vencedor", leilao.getId(),
                        String.format(Locale.US,"{\"leilaoId\": %d, \"clienteId\": %d, \"valor\": %.2f}", 
                        leilao.getId(), vencedor.getClienteId(), vencedor.getValor()));

                    // Solicita Link de Pagamento ao MS Pagamento
                    PagamentoResponseProto pagto = pagamentoStub.solicitarPagamento(
                            PagamentoRequestProto.newBuilder()
                                    .setLeilaoId(leilao.getId())
                                    .setClienteId(vencedor.getClienteId())
                                    .setValor(vencedor.getValor())
                                    .build());

                    // Notifica o Link
                    notificarGateway("link_pagamento", leilao.getId(),
                            String.format("{\"leilaoId\": %d, \"link\": \"%s\", \"clienteId\": %d}",
                            leilao.getId(), pagto.getLinkPagamento(), vencedor.getClienteId()), 
                            vencedor.getClienteId()); // Mensagem privada para o vencedor
                } else {
                    notificarGateway("leilao_finalizado", leilao.getId(), 
                            "{\"mensagem\": \"Sem vencedores\"}");
                }

            } catch (Exception e) {
                System.err.println("Erro na orquestração de finalização: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Auxiliar para enviar notificação (Broadcast)
    private void notificarGateway(String tipo, int leilaoId, String json) {
        notificarGateway(tipo, leilaoId, json, 0);
    }

    // Auxiliar para enviar notificação (Direcionada)
    private void notificarGateway(String tipo, int leilaoId, String json, int clienteId) {
        try {
            gatewayStub.enviarNotificacao(EventoNotificacao.newBuilder()
                    .setTipo(tipo)
                    .setLeilaoId(leilaoId)
                    .setDadosJson(json)
                    .setClienteId(clienteId)
                    .build());
        } catch (Exception e) {
            System.err.println("Erro ao notificar Gateway: " + e.getMessage());
        }
    }

    public Leilao buscarPorId(int id) {
        return leiloes.get(id);
    }

    public Collection<Leilao> listarAtivos() {
        return leiloes.values().stream()
                .filter(l -> l.getStatus() == LeilaoStatus.ATIVO)
                .toList();
    }
    
    public Collection<Leilao> listarTodos() {
        return leiloes.values();
    }
}