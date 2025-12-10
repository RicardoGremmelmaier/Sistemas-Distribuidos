package com.grpc.api_gateway.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterRegistry {

    // Mapa: leilaoId -> (clienteId -> emitter)
    private final Map<Integer, Map<Integer, SseEmitter>> emittersPorLeilao = new ConcurrentHashMap<>();

    public void addEmitter(Integer leilaoId, Integer clienteId, SseEmitter emitter) {
        emittersPorLeilao
            .computeIfAbsent(leilaoId, k -> new ConcurrentHashMap<>())
            .put(clienteId, emitter);
    }

    public void removeEmitter(Integer leilaoId, Integer clienteId) {
        Map<Integer, SseEmitter> clientes = emittersPorLeilao.get(leilaoId);
        if (clientes != null) {
            clientes.remove(clienteId);
            if (clientes.isEmpty()) emittersPorLeilao.remove(leilaoId);
        }
    }

    public Map<Integer, SseEmitter> getEmittersPorLeilao(Integer leilaoId) {
        return emittersPorLeilao.getOrDefault(leilaoId, Map.of());
    }

    public SseEmitter getEmitter(Integer leilaoId, Integer clienteId) {
        Map<Integer, SseEmitter> clientes = emittersPorLeilao.get(leilaoId);
        if (clientes != null) {
            return clientes.get(clienteId);
        }
        return null;
    }

    public void removeAllEmittersDoLeilao(Integer leilaoId) {
        emittersPorLeilao.remove(leilaoId);
    }

    public void removeAll() {
        emittersPorLeilao.clear();
    }
}
