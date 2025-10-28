package com.leilao.api_gateway.model;

import java.time.LocalDateTime;

public class Leilao {
    private Integer leilaoId;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Integer idVencedor;
    private boolean finalizado;

    public Integer getLeilaoId() { return leilaoId; }
    public void setLeilaoId(Integer leilaoId) { this.leilaoId = leilaoId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }

    public Integer getIdVencedor() { return idVencedor; }
    public void setIdVencedor(Integer idVencedor) { this.idVencedor = idVencedor; }

    public boolean isFinalizado() { return finalizado; }
    public void setFinalizado(boolean finalizado) { this.finalizado = finalizado; }
}
