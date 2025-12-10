package com.grpc.ms_pagamento.service;

import com.leilao.common.grpc.notificacoes.EventoNotificacao;
import com.leilao.common.grpc.notificacoes.NotificacaoServiceGrpc;
import com.grpc.ms_pagamento.model.PagamentoRequest;
import com.grpc.ms_pagamento.model.PagamentoResponse;
import com.grpc.ms_pagamento.model.StatusPagamento;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PagamentoService {

    // Cliente gRPC para avisar o Gateway
    @GrpcClient("gateway-client")
    private NotificacaoServiceGrpc.NotificacaoServiceBlockingStub gatewayStub;

    private final RestTemplate restTemplate = new RestTemplate();

    // 1. Método síncrono chamado pelo GrpcPagamentoService (interno)
    public PagamentoResponse solicitarPagamento(PagamentoRequest request) {
        Map<String, Object> payload = Map.of(
            "leilaoId", request.getLeilaoId(),
            "valor", request.getValor(),
            "clienteId", request.getClienteId()
        );

        try {
            // Chamada REST ao sistema externo
            Map responseMap = restTemplate.postForObject(
                    "http://localhost:8084/external-payment/create",
                    payload,
                    Map.class
            );

            String link = (String) responseMap.get("linkPagamento");
            
            // Retorna o objeto simples para o GrpcService empacotar
            return new PagamentoResponse(link, StatusPagamento.PENDENTE);

        } catch (Exception e) {
            System.err.println("Erro no pagamento externo: " + e.getMessage());
            throw new RuntimeException("Erro ao criar pagamento", e);
        }
    }

    // Método chamado pelo Controller REST (Webhook do sistema externo)
    public void atualizarStatusPagamento(int leilaoId, StatusPagamento status, int clienteId) {
        System.out.println("Atualizando status para: " + status);

        try {
            String dadosJson = String.format("{\"leilaoId\": %d, \"status\": \"%s\", \"clienteId\": %d}", 
                    leilaoId, status.name(), clienteId);

            gatewayStub.enviarNotificacao(EventoNotificacao.newBuilder()
                    .setTipo("status_pagamento")
                    .setLeilaoId(leilaoId)
                    .setClienteId(clienteId) // Notificação direcionada
                    .setDadosJson(dadosJson)
                    .build());
                    
        } catch (Exception e) {
            System.err.println("Erro ao notificar gateway via gRPC: " + e.getMessage());
        }
    }
}