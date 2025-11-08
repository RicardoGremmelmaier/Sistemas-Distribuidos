package com.leilao.ms_pagamento.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Evento {
    private String tipo;
    private String mensagem;
    private Object dados;

    public Evento() {}

    public Evento(String tipo, String mensagem, Object dados) {
        this.tipo = tipo;
        this.mensagem = mensagem;
        this.dados = dados;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public Object getDados() { return dados; }
    public void setDados(Object dados) { this.dados = dados; }

    @Override
    public String toString() {
        return "Evento{" +
                "tipo='" + tipo + '\'' +
                ", mensagem='" + mensagem + '\'' +
                ", dados=" + dados +
                '}';
    }
}
