package com.grpc.api_gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Evento {
    private String tipo;
    private String mensagem;
    private Object dados;

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
