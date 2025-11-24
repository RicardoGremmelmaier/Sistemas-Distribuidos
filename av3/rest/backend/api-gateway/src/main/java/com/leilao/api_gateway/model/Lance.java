package com.leilao.api_gateway.model;

public class Lance {
    private Integer leilaoId;
    private Integer clienteId;
    private double valor;

    public Integer getLeilaoId() { return leilaoId; }
    public void setLeilaoId(Integer leilaoId) { this.leilaoId = leilaoId; }

    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
}
