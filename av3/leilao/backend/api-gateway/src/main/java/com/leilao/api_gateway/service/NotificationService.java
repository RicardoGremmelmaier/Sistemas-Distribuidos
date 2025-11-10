package com.leilao.api_gateway.service;

import com.leilao.api_gateway.config.SseEmitterRegistry;
import com.leilao.api_gateway.model.Evento;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
public class NotificationService {

    private final SseEmitterRegistry registry;

    public NotificationService(SseEmitterRegistry registry) {
        this.registry = registry;
    }

    public SseEmitter registrarClienteEmLeilao(Integer leilaoId, Integer clienteId) {
        SseEmitter emitter = new SseEmitter(0L); // nunca expira
        registry.addEmitter(leilaoId, clienteId, emitter);

        emitter.onCompletion(() -> registry.removeEmitter(leilaoId, clienteId));
        emitter.onTimeout(() -> registry.removeEmitter(leilaoId, clienteId));
        emitter.onError((e) -> registry.removeEmitter(leilaoId, clienteId));

        System.out.println("[SSE] Cliente " + clienteId + " registrado no leilão " + leilaoId);
        return emitter;
    }

    public void cancelarInscricaoCliente(Integer leilaoId, Integer clienteId) {
        registry.removeEmitter(leilaoId, clienteId);
        System.out.println("[SSE] Cliente " + clienteId + " cancelou inscrição no leilão " + leilaoId);
    }

    public void cancelarNotificacoesDeLeilao(Integer leilaoId) {
        registry.removeAllEmittersDoLeilao(leilaoId);
        System.out.println("[SSE] Notificações canceladas para o leilão " + leilaoId);
    }

    public void notificarLeilao(Integer leilaoId, Evento evento) {
        Map<Integer, SseEmitter> emitters = registry.getEmittersPorLeilao(leilaoId);

        emitters.forEach((clienteId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(evento.getTipo())
                        .data(evento));
            } catch (IOException e) {
                registry.removeEmitter(leilaoId, clienteId);
                System.err.println("[SSE] Erro ao enviar notificação ao cliente " + clienteId);
            }
        });
    }
    
    public boolean isInscrito(Integer leilaoId, Integer clienteId) {
        Map<Integer, SseEmitter> emitters = registry.getEmittersPorLeilao(leilaoId);
        return emitters.containsKey(clienteId);
    }
}
