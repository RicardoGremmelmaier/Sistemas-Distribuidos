package com.leilao.ms_leilao.model;

import java.time.LocalDateTime;

public class Leilao {
    private Integer id;
    private String nomeDoProduto;
    private String descricao;
    private double lanceInicial;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private LeilaoStatus status;
    private Integer idVencedor;

    public Integer getId() {return id; }
    public void setId(Integer id) {this.id = id; }

    public String getNomeDoProduto() {return nomeDoProduto; }
    public void setNomeDoProduto(String nomeDoProduto) {this.nomeDoProduto = nomeDoProduto; }

    public String getDescricao() {return descricao; }
    public void setDescricao(String descricao) {this.descricao = descricao; }

    public double getLanceInicial() {return lanceInicial; }
    public void setLanceInicial(double lanceInicial) {this.lanceInicial = lanceInicial; }

    public LocalDateTime getDataInicio() {return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) {this.dataInicio = dataInicio; }   

    public LocalDateTime getDataFim() {return dataFim; }
    public void setDataFim(LocalDateTime dataFim) {this.dataFim = dataFim; }

    public LeilaoStatus getStatus() {return status; }
    public void setStatus(LeilaoStatus status) {this.status = status; }

    public Integer getIdVencedor() {return idVencedor; }
    public void setIdVencedor(Integer idVencedor) {this.idVencedor = idVencedor; }

}
