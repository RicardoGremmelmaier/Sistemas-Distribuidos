package com.grpc.ms_lance.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LanceValidatorService {

    private final Map<Integer, Double> maiorLancePorLeilao = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> vencedorPorLeilao = new ConcurrentHashMap<>();
    private final Set<Integer> leiloesAtivos = ConcurrentHashMap.newKeySet();

    public void ativarLeilao(int leilaoId) {
        leiloesAtivos.add(leilaoId);
        maiorLancePorLeilao.put(leilaoId, 0.0);
        vencedorPorLeilao.remove(leilaoId);
        System.out.println("Leilão " + leilaoId + " ativado.");
    }

    public void finalizarLeilao(int leilaoId) {
        leiloesAtivos.remove(leilaoId);
        System.out.println("Leilão " + leilaoId + " finalizado, não aceita mais lances.");
    }

    public boolean estaAtivo(int leilaoId) {
        return leiloesAtivos.contains(leilaoId);
    }

    public boolean processarNovoLance(int leilaoId, int clienteId, double valor) {
        if (!estaAtivo(leilaoId)) return false;

        double valorAtual = maiorLancePorLeilao.getOrDefault(leilaoId, 0.0);
        if (valor <= valorAtual) return false;

        maiorLancePorLeilao.put(leilaoId, valor);
        vencedorPorLeilao.put(leilaoId, clienteId);
        return true;
    }

    public Double getMaiorLance(int leilaoId) {
        return maiorLancePorLeilao.get(leilaoId);
    }

    public Integer getVencedor(int leilaoId) {
        return vencedorPorLeilao.get(leilaoId);
    }
}
