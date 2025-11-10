    package com.leilao.ms_leilao.service;

    import org.springframework.context.annotation.Lazy;
    import org.springframework.scheduling.TaskScheduler;
    import org.springframework.stereotype.Service;

    import java.time.Instant;
    import java.time.LocalDateTime;
    import java.time.ZoneId;
    import java.util.concurrent.ScheduledFuture;

    @Service
    public class SchedulerService {

        private final TaskScheduler scheduler;
        private final MsLeilaoService leilaoService;

        private final java.util.concurrent.ConcurrentHashMap<Integer, ScheduledFuture<?>> scheduledStarts = new java.util.concurrent.ConcurrentHashMap<>();
        private final java.util.concurrent.ConcurrentHashMap<Integer, ScheduledFuture<?>> scheduledEnds = new java.util.concurrent.ConcurrentHashMap<>();

        public SchedulerService(TaskScheduler scheduler, @Lazy MsLeilaoService leilaoService) {
            this.scheduler = scheduler;
            this.leilaoService = leilaoService;
        }

        public void agendarInicio(int leilaoId, LocalDateTime dataInicio) {
            ZoneId zone = ZoneId.systemDefault();
            Instant when = dataInicio.atZone(zone).toInstant();
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

        public void agendarFim(int leilaoId, LocalDateTime dataFim) {
            ZoneId zone = ZoneId.systemDefault();
            Instant when = dataFim.atZone(zone).toInstant();
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

