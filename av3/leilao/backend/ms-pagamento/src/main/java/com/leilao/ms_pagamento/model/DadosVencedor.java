package com.leilao.ms_pagamento.model;

public class DadosVencedor {

    private int leilaoId;
    private int vencedorId;
    private double valor;

    public DadosVencedor() {}

    public DadosVencedor(int leilaoId, int vencedorId, double valor) {
        this.leilaoId = leilaoId;
        this.vencedorId = vencedorId;
        this.valor = valor;
    }

    public int getLeilaoId() { return leilaoId; }
    public void setLeilaoId(int leilaoId) { this.leilaoId = leilaoId; }

    public int getVencedorId() { return vencedorId; }
    public void setVencedorId(int vencedorId) { this.vencedorId = vencedorId; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    @Override
    public String toString() {
        return "DadosVencedor{" +
                "leilaoId=" + leilaoId +
                ", vencedorId=" + vencedorId +
                ", valor=" + valor +
                '}';
    }
}
