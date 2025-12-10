package com.grpc.ms_lance.service;

import com.leilao.common.grpc.notificacoes.EventoNotificacao;
import com.leilao.common.grpc.notificacoes.NotificacaoServiceGrpc;
import com.leilao.common.grpc.lance.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService 
public class GrpcLanceService extends LanceServiceGrpc.LanceServiceImplBase {

    @Autowired
    private LanceValidatorService validator;

    // Injeta o cliente para falar com o Gateway (definido no application.properties)
    @GrpcClient("gateway-client")
    private NotificacaoServiceGrpc.NotificacaoServiceBlockingStub gatewayStub;

    @Override
    public void processarLance(LanceRequest request, StreamObserver<LanceResponse> responseObserver) {
        boolean aceito = validator.processarNovoLance(
                request.getLeilaoId(),
                request.getClienteId(),
                request.getValor()
        );

        // Retorna a resposta Síncrona para quem chamou (o Gateway REST)
        LanceResponse response = LanceResponse.newBuilder()
                .setValido(aceito)
                .setMotivo(aceito ? "Lance aceito" : "Lance inválido ou menor que o atual")
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        // Notificação Assíncrona via gRPC para o Gateway atualizar o Front (SSE)
        if (aceito) {
            try {
                // Monta um JSON simples na mão para o front
                String jsonDados = String.format("{\"leilaoId\": %d, \"valor\": %.2f, \"clienteId\": %d}", 
                        request.getLeilaoId(), request.getValor(), request.getClienteId());

                gatewayStub.enviarNotificacao(EventoNotificacao.newBuilder()
                        .setTipo("lance_validado")
                        .setLeilaoId(request.getLeilaoId())
                        .setDadosJson(jsonDados)
                        .build());
            } catch (Exception e) {
                System.err.println("Erro ao notificar gateway: " + e.getMessage());
            }
        }
    }

    @Override
    public void obterVencedor(LeilaoIdRequest request, StreamObserver<VencedorResponse> responseObserver) {
        Integer vencedorId = validator.getVencedor(request.getId());
        Double valor = validator.getMaiorLance(request.getId());

        VencedorResponse.Builder builder = VencedorResponse.newBuilder();
        
        if (vencedorId != null) {
            builder.setTemVencedor(true)
                   .setClienteId(vencedorId)
                   .setValor(valor);
        } else {
            builder.setTemVencedor(false);
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void iniciarLeilao(LeilaoDados request, StreamObserver<Empty> responseObserver) {
        validator.ativarLeilao(request.getId());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void finalizarLeilao(LeilaoIdRequest request, StreamObserver<Empty> responseObserver) {
        validator.finalizarLeilao(request.getId());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}