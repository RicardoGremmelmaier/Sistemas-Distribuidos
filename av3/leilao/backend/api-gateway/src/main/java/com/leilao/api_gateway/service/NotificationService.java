package com.leilao.api_gateway.service;

import com.leilao.api_gateway.config.SseEmitterRegistry;
import com.leilao.api_gateway.model.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class NotificationService {

    @Autowired
    private SseEmitterRegistry registry;

    public SseEmitter registrarCliente (Integer clienteID) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        registry.addEmitter(clienteID, emitter);
        emitter.onCompletion(() -> registry.removeEmitter(clienteID));
        emitter.onTimeout(() -> registry.removeEmitter(clienteID));
        emitter.onError(e -> registry.removeEmitter(clienteID));

        return emitter;
    }

    public void notificarCliente (Integer clienteID, Evento evento) {
        SseEmitter emitter = registry.getEmitter(clienteID);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                                       .name(evento.getTipo())
                                       .data(evento));
            } catch (Exception e) {
                registry.removeEmitter(clienteID);
            }
        }
    }

    public void cancelarNotificacoes (Integer clienteID) {
        SseEmitter emitter = registry.getEmitter(clienteID);
        if (emitter != null) {
            try {
                emitter.complete();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                registry.removeEmitter(clienteID);
            }
        }
    }

}
