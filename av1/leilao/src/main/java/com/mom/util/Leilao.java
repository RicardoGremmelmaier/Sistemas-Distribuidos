package com.mom.util;

import java.time.*;
import java.util.Timer;
import java.util.TimerTask;

public class Leilao extends TimerTask{
    private final int leilao_id;
    private final String descricao;
    private final LocalDateTime data_inicio;
    private final LocalDateTime data_fim;
    private boolean isFinished;

    private static int id_counter = 1;

    public Leilao(String descricao) {
        this.leilao_id = id_counter++;
        this.descricao = descricao;
        this.data_inicio = LocalDateTime.now();
        this.data_fim = data_inicio.plusMinutes(2);
        this.isFinished = false;

        Timer timer = new Timer();
        timer.schedule(this, Duration.between(this.data_inicio, this.data_fim).toMillis());
    }

    @Override
    public void run() {
        finalizarLeilao();
    }

    public int getLeilaoId() {
        return leilao_id;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataInicio() {
        return data_inicio;
    }

    public LocalDateTime getDataFim() {
        return data_fim;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void finalizarLeilao() {
        this.isFinished = true;
        System.out.println("Leilão " + this.leilao_id + " finalizado.");
    } 

}
