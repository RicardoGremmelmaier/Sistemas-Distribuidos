package com.mom.util;

import java.time.*;
import java.util.Random;

public class Leilao{
    private final int leilaoId;
    private final String descricao;
    private final LocalDateTime dataInicio;
    private final LocalDateTime dataFim;
    private boolean finalizado;

    private static int idCounter = 1;

    public Leilao(String descricao, int additionalMinutes) {
        this.leilaoId = idCounter++;
        this.descricao = descricao;
        this.dataInicio = LocalDateTime.now().plusSeconds(new Random().nextInt(30));
        this.dataFim = dataInicio.plusMinutes(additionalMinutes);
        this.finalizado = false;
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
    }


    @Override
    public String toString() {
        int durationMinutes = (int) Duration.between(dataInicio, dataFim).toMinutes();
        return "{" +
                "\"leilaoId\":" + leilaoId +
                ", \"descricao\":\"" + descricao + "\"" +
                ", \"dataInicio\":\"" + dataInicio + "\"" +
                ", \"dataFim\":\"" + dataFim + "\"" +
                ", \"durationMinutes\":" + durationMinutes +
                ", \"finalizado\":" + finalizado +
                '}';
    }

}
