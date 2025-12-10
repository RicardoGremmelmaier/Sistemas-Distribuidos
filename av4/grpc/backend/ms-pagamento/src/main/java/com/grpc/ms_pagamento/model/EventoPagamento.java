package com.grpc.ms_pagamento.model;

public class EventoPagamento {
    private String tipo;
    private String mensagem;
    private Object dados;

    public EventoPagamento() {}

    public EventoPagamento(String tipo, String mensagem, Object dados) {
        this.tipo = tipo;
        this.mensagem = mensagem;
        this.dados = dados;
    }

    public String getTipo() {return tipo; }
    public void setTipo(String tipo) {this.tipo = tipo; }

    public String getMensagem() {return mensagem; }
    public void setMensagem(String mensagem) {this.mensagem = mensagem; }

    public Object getDados() {return dados; }
    public void setDados(Object dados) {this.dados = dados; }
    
}