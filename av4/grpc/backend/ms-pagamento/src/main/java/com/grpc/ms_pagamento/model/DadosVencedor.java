package com.leilao.ms_pagamento.model;

public class DadosVencedor {

    private int leilaoId;
    private int clienteId;
    private double valor;

    public DadosVencedor() {}

    public DadosVencedor(int leilaoId, int clienteId, double valor) {
        this.leilaoId = leilaoId;
        this.clienteId = clienteId;
        this.valor = valor;
    }

    public int getLeilaoId() { return leilaoId; }
    public void setLeilaoId(int leilaoId) { this.leilaoId = leilaoId; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    @Override
    public String toString() {
        return "DadosVencedor{" +
                "leilaoId=" + leilaoId +
                ", clienteId=" + clienteId +
                ", valor=" + valor +
                '}';
    }
}
