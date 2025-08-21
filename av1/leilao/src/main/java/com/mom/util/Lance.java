package com.mom.util;

public class Lance {
    private final int leilao_id;
    private final int cliente_id;
    private final double valor;
    private final String hash;


    public Lance(int leilao_id, int cliente_id, double valor, String hash) {
        this.leilao_id = leilao_id;
        this.cliente_id = cliente_id;
        this.valor = valor;
        this.hash = hash;
    }

    public int getLeilaoId() {
        return leilao_id;
    }

    public int getClienteId() {
        return cliente_id;
    }

    public double getValor() {
        return valor;
    }

    public String getHash() {
        return hash;
    }

}
