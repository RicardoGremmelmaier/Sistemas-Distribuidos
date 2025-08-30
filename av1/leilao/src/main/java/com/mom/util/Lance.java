package com.mom.util;

public class Lance {
    private final int leilao_id;
    private final int cliente_id;
    private final double valor;
    private final byte[] assinatura;

    public Lance(int leilao_id, int cliente_id, double valor, byte[] assinatura) {
        this.leilao_id = leilao_id;
        this.cliente_id = cliente_id;
        this.valor = valor;
        this.assinatura = assinatura;
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

    public byte[] getAssinatura() {
        return assinatura;
    }

}
