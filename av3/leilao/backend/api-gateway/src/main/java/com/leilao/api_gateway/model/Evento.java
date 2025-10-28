package com.leilao.api_gateway.model;

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
}
