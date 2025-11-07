package com.leilao.ms_pagamento.model;

public class PagamentoRequest {
    private int idLeilao;
    private int idCliente;
    private double valor;
    private String moeda = "BRL";

    public PagamentoRequest() {}

    public PagamentoRequest(int idLeilao, int idCliente, double valor) {
        this.idLeilao = idLeilao;
        this.idCliente = idCliente;
        this.valor = valor;
    }

    public int getIdLeilao() {return idLeilao; }
    public void setIdLeilao(int idLeilao) {this.idLeilao = idLeilao; }

    public int getIdCliente() {return idCliente; }
    public void setIdCliente(int idCliente) {this.idCliente = idCliente; }

    public double getValor() {return valor; }
    public void setValor(double valor) {this.valor = valor; }

    public String getMoeda() {return moeda; }
    public void setMoeda(String moeda) {this.moeda = moeda; }

}