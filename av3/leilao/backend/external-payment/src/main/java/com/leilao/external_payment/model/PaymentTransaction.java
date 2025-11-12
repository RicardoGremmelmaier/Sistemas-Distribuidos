package com.leilao.external_payment.model;

import java.util.Map;

public class PaymentTransaction {

    private String idTransacao;
    private int leilaoId;
    private double valor;
    private String status;
    private Map<?, ?> dadosCliente;

    public PaymentTransaction(String idTransacao, int leilaoId, double valor, String status, Map<?, ?> dadosCliente) {
        this.idTransacao = idTransacao;
        this.leilaoId = leilaoId;
        this.valor = valor;
        this.status = status;
        this.dadosCliente = dadosCliente;
    }

    public String getIdTransacao() { return idTransacao; }
    public int getLeilaoId() { return leilaoId; }
    public double getValor() { return valor; }
    public String getStatus() { return status; }
    public Map<?, ?> getDadosCliente() { return dadosCliente; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Transação{id='%s', leilaoId=%d, valor=%.2f, status='%s'}",
                idTransacao, leilaoId, valor, status);
    }
}
