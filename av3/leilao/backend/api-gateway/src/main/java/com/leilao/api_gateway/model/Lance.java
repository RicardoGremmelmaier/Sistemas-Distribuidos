package com.leilao.api_gateway.model;

public class Lance {
    private Integer leilaoId;
    private Integer clienteId;
    private double valor;

    public Integer getLeilaoId() { return leilaoId; }
    public void setIdLeilao(Integer idLeilao) { this.leilaoId = idLeilao; }

    public Integer getIdUsuario() { return clienteId; }
    public void setIdUsuario(Integer idUsuario) { this.clienteId = idUsuario; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
}
