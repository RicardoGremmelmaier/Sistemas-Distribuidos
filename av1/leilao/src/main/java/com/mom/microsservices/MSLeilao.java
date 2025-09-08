package com.mom.microsservices;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.mom.rabbit.Publisher;
import com.mom.util.Leilao;

public class MSLeilao{
    private Publisher publisher;

    private final Map<Integer, Leilao> leiloes = new HashMap<>();
    private final Map<Integer, Timer> timers = new HashMap<>();

    private final String routingLeilaoIniciado = "leilao.iniciado";
    private final String routingLeilaoFinalizado = "leilao.finalizado";

    public MSLeilao() {
        try {
            this.publisher = new Publisher();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar publisher", e);
        }
    }

    public void criarLeilao(String descricao, int additionalMinutes) {
        Leilao leilao = new Leilao(descricao, additionalMinutes);
        leiloes.put(leilao.getLeilaoId(), leilao);
        System.out.println("Leilão " + leilao.getLeilaoId() + " iniciado.");

        try {
            publisher.publish(routingLeilaoIniciado, leilao.toString());
        } catch (Exception e) {
            System.err.println("Erro ao publicar mensagem de leilão iniciado: " + e.getMessage());
        }

        Timer timer = new Timer();
        long delay = Duration.between(leilao.getDataInicio(), leilao.getDataFim()).toMillis();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finalizarLeilao(leilao);
            }
        }, delay);

        timers.put(leilao.getLeilaoId(), timer);

    }

    public void finalizarLeilao(Leilao leilao) {
        try {
            publisher.publish(routingLeilaoFinalizado, "Leilão " + leilao.getLeilaoId() + " finalizado.");
        } catch (Exception e) {
            System.err.println("Erro ao publicar mensagem de leilão finalizado: " + e.getMessage());
        }
        
        leilao.finalizarLeilao();
    }

    public static void main(String[] args){
        MSLeilao msLeilao = new MSLeilao();

        msLeilao.criarLeilao("saveiro 2009 1.6 bruxa", 1);
        msLeilao.criarLeilao("novilha nelore 18 arroba", 2);

    }

}
