package com.leilao.ms_leilao.service;

import com.leilao.ms_leilao.messaging.LeilaoPublisher;
import com.leilao.ms_leilao.model.EventoLeilao;
import com.leilao.ms_leilao.model.Leilao;
import com.leilao.ms_leilao.model.LeilaoStatus;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MsLeilaoService {

    private final Map<Integer, Leilao> leiloes = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    private final SchedulerService schedulerService;
    private final LeilaoPublisher publisher;

    private final ConcurrentHashMap<Integer, Object> locks = new ConcurrentHashMap<>();

    public MsLeilaoService(SchedulerService schedulerService, LeilaoPublisher publisher) {
        this.schedulerService = schedulerService;
        this.publisher = publisher;
    }

    public Leilao criarLeilao(Leilao input) {
        int id = input.getId() == 0 ? idGenerator.getAndIncrement() : input.getId();
        input.setId(id);
        input.setStatus(LeilaoStatus.PENDENTE);

        if (input.getDataInicio() == null || input.getDataFim() == null) {
            throw new IllegalArgumentException("dataInicio e dataFim são obrigatórios e em formato ISO-8601");
        }

        leiloes.put(id, input);

        OffsetDateTime now = OffsetDateTime.now();
        if (!input.getDataInicio().isAfter(now)) {
            ativarLeilao(input);
        } else {
            schedulerService.agendarInicio(input.getId(), input.getDataInicio());
        }
        schedulerService.agendarFim(input.getId(), input.getDataFim());

        System.out.println("Leilão criado: id=" + id + " nome=" + input.getNomeProduto());
        return input;
    }

    public void ativarLeilao(Leilao leilao) {
        synchronized (getLockForLeilao(leilao.getId())) {
            if (leilao.getStatus() == LeilaoStatus.PENDENTE) {
                leilao.setStatus(LeilaoStatus.ATIVO);
                EventoLeilao evento = new EventoLeilao(
                        "leilao_iniciado",
                        "Leilão iniciado: " + leilao.getId(),
                        Map.of(
                                "idLeilao", leilao.getId(),
                                "nomeProduto", leilao.getNomeProduto(),
                                "dataInicio", leilao.getDataInicio().toString(),
                                "dataFim", leilao.getDataFim().toString()
                        )
                );
                publisher.publish("leilao.iniciado", evento);
            }
        }
    }

    public void finalizarLeilao(Leilao leilao) {
        synchronized (getLockForLeilao(leilao.getId())) {
            if (leilao.getStatus() != LeilaoStatus.FINALIZADO) {
                leilao.setStatus(LeilaoStatus.FINALIZADO);
                EventoLeilao evento = new EventoLeilao(
                        "leilao_finalizado",
                        "Leilão finalizado: " + leilao.getId(),
                        Map.of(
                                "idLeilao", leilao.getId(),
                                "dataFim", leilao.getDataFim().toString()
                        )
                );
                publisher.publish("leilao.finalizado", evento);
            }
        }
    }

    public Leilao buscarPorId(int id) {
        return leiloes.get(id);
    }

    public Collection<Leilao> listarAtivos() {
        return leiloes.values().stream()
                .filter(l -> l.getStatus() == LeilaoStatus.ATIVO)
                .toList();
    }

    private Object getLockForLeilao(int id) {
        return locks.computeIfAbsent(id, k -> new Object());
    }
}