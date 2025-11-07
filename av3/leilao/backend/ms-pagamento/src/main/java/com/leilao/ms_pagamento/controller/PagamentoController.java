package com.leilao.ms_pagamento.controller;

import com.leilao.ms_pagamento.model.StatusPagamento;
import com.leilao.ms_pagamento.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {
    
    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestBody CallbackRequest req) {
        pagamentoService.atualizarStatusPagamento(req.getIdLeilao(), req.getStatus());
        return ResponseEntity.ok("Status processado com sucesso");
    }

    // Classe interna apenas para simplificar o payload do callback
    public static class CallbackRequest {
        private int idLeilao;
        private StatusPagamento status;

        public int getIdLeilao() { return idLeilao; }
        public void setIdLeilao(int idLeilao) { this.idLeilao = idLeilao; }
        public StatusPagamento getStatus() { return status; }
        public void setStatus(StatusPagamento status) { this.status = status; }
    }
}