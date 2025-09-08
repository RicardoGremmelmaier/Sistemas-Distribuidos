package com.mom.util;

public class Lance {
    private final int leilaoId;
    private final int clienteId;
    private final double valor;
    private byte[] assinatura;

    public Lance(int leilaoId, int clienteId, double valor) {
        this.leilaoId = leilaoId;
        this.clienteId = clienteId;
        this.valor = valor;
    }

    public int getLeilaoId() {
        return leilaoId;
    }

    public int getClienteId() {
        return clienteId;
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
                "leilaoId=" + leilaoId +
                ", clienteId=" + clienteId +
                ", valor=" + valor +
                '}';
    }

}
