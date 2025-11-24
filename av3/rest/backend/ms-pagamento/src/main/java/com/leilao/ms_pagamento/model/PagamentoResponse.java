package com.leilao.ms_pagamento.model;

public class PagamentoResponse {
    private String linkPagamento;
    private StatusPagamento status;

    public PagamentoResponse() {}

    public PagamentoResponse(String linkPagamento, StatusPagamento status) {
        this.linkPagamento = linkPagamento;
        this.status = status;
    }

    public String getLinkPagamento() { return linkPagamento; }
    public void setLinkPagamento(String linkPagamento) { this.linkPagamento = linkPagamento; }

    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }
    
}