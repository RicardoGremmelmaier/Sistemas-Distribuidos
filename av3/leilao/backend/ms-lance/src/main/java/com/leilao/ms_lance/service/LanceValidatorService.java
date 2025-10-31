package com.leilao.ms_lance.service;

import com.leilao.ms_lance.model.Lance;
import com.leilao.ms_lance.model.ResultadoLance;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LanceValidatorService {

    private final Map<Integer, Double> maiorLancePorLeilao = new ConcurrentHashMap<>();
    private final Set<Integer> leiloesAtivos = ConcurrentHashMap.newKeySet();

    public void ativarLeilao(int idLeilao) {
        leiloesAtivos.add(idLeilao);
        System.out.println("Leilão " + idLeilao + " ativado para receber lances.");
    }

    public void finalizarLeilao(int idLeilao) {
        leiloesAtivos.remove(idLeilao);
        System.out.println("Leilão " + idLeilao + " finalizado, não aceita mais lances.");
    }

    public boolean estaAtivo(int idLeilao) {
        return leiloesAtivos.contains(idLeilao);
    }

    public ResultadoLance validar(Lance lance) {
        if (!estaAtivo(lance.getLeilaoId())) {
            return new ResultadoLance(false, "Leilão não está ativo");
        }

        double valorAtual = maiorLancePorLeilao.getOrDefault(lance.getLeilaoId(), 0.0);
        if (lance.getValor() <= valorAtual) {
            return new ResultadoLance(false, "Lance menor ou igual ao atual");
        }
        maiorLancePorLeilao.put(lance.getLeilaoId(), lance.getValor());
        return new ResultadoLance(true, "Lance aceito");
    }

    public Double getMaiorLance(int idLeilao) {
        return maiorLancePorLeilao.get(idLeilao);
    }
}
