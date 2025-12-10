package com.grpc.ms_pagamento.model;

public class PagamentoRequest {
    private int leilaoId;
    private int clienteId;
    private double valor;
    private String moeda = "BRL";

    public PagamentoRequest() {}

    public PagamentoRequest(int leilaoId, int clienteId, double valor) {
        this.leilaoId = leilaoId;
        this.clienteId = clienteId;
        this.valor = valor;
    }

    public int getLeilaoId() {return leilaoId; }
    public void setLeilaoId(int leilaoId) {this.leilaoId = leilaoId; }

    public int getClienteId() {return clienteId; }
    public void setClienteId(int clienteId) {this.clienteId = clienteId; }

    public double getValor() {return valor; }
    public void setValor(double valor) {this.valor = valor; }

    public String getMoeda() {return moeda; }
    public void setMoeda(String moeda) {this.moeda = moeda; }

}