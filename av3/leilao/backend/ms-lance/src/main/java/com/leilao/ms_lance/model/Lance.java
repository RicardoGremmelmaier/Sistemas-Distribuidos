package com.leilao.ms_lance.model;

public class Lance {
    private int clienteId;
    private int leilaoId;
    private double valor;

    public Lance() {}

    public Lance(int clienteId, int leilaoId, double valor) {
        this.clienteId = clienteId;
        this.leilaoId = leilaoId;
        this.valor = valor;
    }

    public int getClienteId() {return clienteId; }
    public void setClienteId(int clienteId) {this.clienteId = clienteId; }

    public int getLeilaoId() {return leilaoId; }
    public void setLeilaoId(int leilaoId) {this.leilaoId = leilaoId; }

    public double getValor() {return valor; }
    public void setValor(double valor) {this.valor = valor; }

}
