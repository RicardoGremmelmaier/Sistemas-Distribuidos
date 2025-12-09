package com.leilao.external_payment.model;

public class PaymentTransaction {

    private String idTransacao;
    private int leilaoId;
    private double valor;
    private String status;
    private int clienteId;

    public PaymentTransaction(String idTransacao, int leilaoId, double valor, String status, int clienteId) {
        this.idTransacao = idTransacao;
        this.leilaoId = leilaoId;
        this.valor = valor;
        this.status = status;
        this.clienteId = clienteId;
    }

    public String getIdTransacao() { return idTransacao; }
    public int getLeilaoId() { return leilaoId; }
    public double getValor() { return valor; }
    public String getStatus() { return status; }
    public int getClienteId() { return clienteId; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Transação{id='%s', leilaoId=%d, valor=%.2f, status='%s'}",
                idTransacao, leilaoId, valor, status);
    }
}
