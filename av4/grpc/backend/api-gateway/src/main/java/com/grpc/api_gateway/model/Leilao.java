package com.grpc.api_gateway.model;

import java.time.LocalDateTime;

public class Leilao {
    private Integer leilaoId;
    private String nomeDoProduto;
    private String descricao;
    private String status;
    private Double lanceInicial;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Integer idVencedor;
    private boolean finalizado;

    public Integer getLeilaoId() { return leilaoId; }
    public void setLeilaoId(Integer leilaoId) { this.leilaoId = leilaoId; }

    public String getNomeDoProduto() { return nomeDoProduto; }
    public void setNomeDoProduto(String nomeDoProduto) { this.nomeDoProduto = nomeDoProduto; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getLanceInicial() { return lanceInicial; }
    public void setLanceInicial(Double lanceInicial) { this.lanceInicial = lanceInicial; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }

    public Integer getIdVencedor() { return idVencedor; }
    public void setIdVencedor(Integer idVencedor) { this.idVencedor = idVencedor; }

    public boolean isFinalizado() { return finalizado; }
    public void setFinalizado(boolean finalizado) { this.finalizado = finalizado; }
}
