package com.grpc.api_gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grpc.api_gateway.model.Evento;
import com.leilao.common.grpc.gateway.Empty;
import com.leilao.common.grpc.gateway.EventoNotificacao;
import com.leilao.common.grpc.gateway.NotificacaoServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class GrpcNotificacaoServer extends NotificacaoServiceGrpc.NotificacaoServiceImplBase {

    @Autowired
    private NotificationService notificationService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void enviarNotificacao(EventoNotificacao request, StreamObserver<Empty> responseObserver) {
        try {
            // Converte o Proto para o Modelo interno do Gateway (Evento)
            // O campo 'dadosJson' vem como String JSON dos microsserviços
            Object dadosConvertidos = null;
            if (request.getDadosJson() != null && !request.getDadosJson().isEmpty()) {
                dadosConvertidos = mapper.readValue(request.getDadosJson(), Object.class);
            }

            Evento evento = new Evento();
            evento.setTipo(request.getTipo());
            evento.setMensagem(request.getMensagem());
            evento.setDados(dadosConvertidos);

            // Decide se é Broadcast (para o leilão todo) ou Unicast (para um cliente)
            if (request.getClienteId() > 0) {
                // Notificação privada (ex: Link de pagamento)
                System.out.println("[Gateway-gRPC] Enviando msg privada para cliente " + request.getClienteId());
                notificationService.notificarCliente(
                        request.getLeilaoId(),
                        request.getClienteId(),
                        evento
                );
            } else {
                // Notificação pública (ex: Novo lance, Leilão finalizado)
                System.out.println("[Gateway-gRPC] Broadcast para leilão " + request.getLeilaoId());
                notificationService.notificarLeilao(
                        request.getLeilaoId(),
                        evento
                );
            }

        } catch (Exception e) {
            System.err.println("[Gateway-gRPC] Erro ao processar notificação: " + e.getMessage());
        }

        // Responde "OK" para o microsserviço que chamou
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}