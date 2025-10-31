package com.leilao.ms_leilao.model;

import java.time.OffsetDateTime;

public class Leilao {
    private int id;
    private String nomeProduto;
    private String descricao;
    private double valorInicial;
    private OffsetDateTime dataInicio;
    private OffsetDateTime dataFim;
    private LeilaoStatus status;
    private Integer idVencedor;

    public int getId() {return id; }
    public void setId(int id) {this.id = id; }

    public String getNomeProduto() {return nomeProduto; }
    public void setNomeProduto(String nomeProduto) {this.nomeProduto = nomeProduto; }

    public String getDescricao() {return descricao; }
    public void setDescricao(String descricao) {this.descricao = descricao; }

    public double getValorInicial() {return valorInicial; }
    public void setValorInicial(double valorInicial) {this.valorInicial = valorInicial; }

    public OffsetDateTime getDataInicio() {return dataInicio; }
    public void setDataInicio(OffsetDateTime dataInicio) {this.dataInicio = dataInicio; }   

    public OffsetDateTime getDataFim() {return dataFim; }
    public void setDataFim(OffsetDateTime dataFim) {this.dataFim = dataFim; }

    public LeilaoStatus getStatus() {return status; }
    public void setStatus(LeilaoStatus status) {this.status = status; }

    public Integer getIdVencedor() {return idVencedor; }
    public void setIdVencedor(Integer idVencedor) {this.idVencedor = idVencedor; }

}
