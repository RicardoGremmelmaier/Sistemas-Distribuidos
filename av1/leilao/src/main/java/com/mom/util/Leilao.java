package com.mom.util;

import java.time.*;

public class Leilao {
    private final int leilao_id;
    private final String descricao;
    private final LocalDateTime data_inicio;
    private final LocalDateTime data_fim;

    private static int id_counter = 1;

    public Leilao(int cliente_id, String descricao, LocalDateTime data_inicio, LocalDateTime data_fim) {
        this.leilao_id = id_counter++;
        this.descricao = descricao;
        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
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

}
