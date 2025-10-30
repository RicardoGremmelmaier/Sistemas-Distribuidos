package com.leilao.api_gateway.controller;

import com.leilao.api_gateway.model.Leilao;
import com.leilao.api_gateway.service.LeilaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leiloes")
public class LeilaoController {

    @Autowired
    private LeilaoService leilaoService;

    @PostMapping
    public ResponseEntity<String> criarLeilao(@RequestBody Leilao leilao) {
        leilaoService.criarLeilao(leilao);
        return ResponseEntity.ok("Leilão criado com sucesso");
    }

    @GetMapping("/ativos")
    public ResponseEntity<Object> listarLeiloesAtivos() {
        return ResponseEntity.ok(leilaoService.listarLeiloes());
    }

}
