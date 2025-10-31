package com.leilao.ms_lance.service;

import com.leilao.ms_lance.model.Lance;
import com.leilao.ms_lance.model.ResultadoLance;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LanceValidatorService {

    private final Map<Integer, Double> maiorLancePorLeilao = new ConcurrentHashMap<>();

    public ResultadoLance validar(Lance lance) {
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
