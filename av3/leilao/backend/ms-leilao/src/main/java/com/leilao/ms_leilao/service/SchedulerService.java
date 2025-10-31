package com.leilao.ms_leilao.service;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.concurrent.ScheduledFuture;

@Service
public class SchedulerService {

    private final TaskScheduler scheduler;
    private final MsLeilaoService leilaoService;

    private final java.util.concurrent.ConcurrentHashMap<Integer, ScheduledFuture<?>> scheduledStarts = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.concurrent.ConcurrentHashMap<Integer, ScheduledFuture<?>> scheduledEnds = new java.util.concurrent.ConcurrentHashMap<>();

    public SchedulerService(TaskScheduler scheduler, MsLeilaoService leilaoService) {
        this.scheduler = scheduler;
        this.leilaoService = leilaoService;
    }

    public void agendarInicio(int leilaoId, OffsetDateTime dataInicio) {
        Instant when = dataInicio.toInstant();
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            var l = leilaoService.buscarPorId(leilaoId);
            if (l != null) {
                leilaoService.ativarLeilao(l);
            }
            scheduledStarts.remove(leilaoId);
        }, when);
        scheduledStarts.put(leilaoId, future);
        System.out.println("Agendado inicio leilao " + leilaoId + " em " + when);
    }

    public void agendarFim(int leilaoId, OffsetDateTime dataFim) {
        Instant when = dataFim.toInstant();
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            var l = leilaoService.buscarPorId(leilaoId);
            if (l != null) {
                leilaoService.finalizarLeilao(l);
            }
            scheduledEnds.remove(leilaoId);
        }, when);
        scheduledEnds.put(leilaoId, future);
        System.out.println("Agendado fim leilao " + leilaoId + " em " + when);
    }
}

