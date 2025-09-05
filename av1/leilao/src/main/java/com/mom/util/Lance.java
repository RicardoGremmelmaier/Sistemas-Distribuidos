package com.mom.util;

public class Lance {
    private final int leilao_id;
    private final int cliente_id;
    private final double valor;
    private byte[] assinatura;

    public Lance(int leilao_id, int cliente_id, double valor) {
        this.leilao_id = leilao_id;
        this.cliente_id = cliente_id;
        this.valor = valor;
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

    public void setAssinatura(byte[] assinatura) {
        this.assinatura = assinatura;
    }

    @Override
    public String toString() {
        return "Lance{" +
                "leilao_id=" + leilao_id +
                ", cliente_id=" + cliente_id +
                ", valor=" + valor +
                '}';
    }

}
