package com.mom.util;

import java.time.*;
import java.util.Timer;
import java.util.TimerTask;

public class Leilao extends TimerTask{
    private final int leilaoId;
    private final String descricao;
    private final LocalDateTime dataInicio;
    private final LocalDateTime dataFim;
    private boolean finalizado;

    private static int id_counter = 1;

    public Leilao(String descricao) {
        this.leilaoId = id_counter++;
        this.descricao = descricao;
        this.dataInicio = LocalDateTime.now();
        this.dataFim = dataInicio.plusMinutes(2);
        this.finalizado = false;

        Timer timer = new Timer();
        timer.schedule(this, Duration.between(this.dataInicio, this.dataFim).toMillis());
    }

    @Override
    public void run() {
        finalizarLeilao();
    }

    public int getLeilaoId() {
        return leilaoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void finalizarLeilao() {
        this.finalizado = true;
        System.out.println("Leilão " + this.leilaoId + " finalizado.");
    } 

    @Override
    public String toString() {
        return "Leilão{" +
                "leilaoId=" + leilaoId +
                ", descricao='" + descricao + '\'' +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", finalizado=" + finalizado +
                '}';
    }

}
