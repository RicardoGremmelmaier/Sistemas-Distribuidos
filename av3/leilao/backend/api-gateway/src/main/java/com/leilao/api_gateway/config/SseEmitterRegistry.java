package com.leilao.api_gateway.config;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class SseEmitterRegistry {
    private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void addEmitter(Integer clienteId, SseEmitter emitter) {
        emitters.put(clienteId, emitter);
    }

    public SseEmitter getEmitter(Integer clienteId) {
        return emitters.get(clienteId);
    }

    public void removeEmitter(Integer clienteId) {
        emitters.remove(clienteId);
    }

    public Map<Integer, SseEmitter> getAllEmitters() {
        return emitters;
    }
    
}
