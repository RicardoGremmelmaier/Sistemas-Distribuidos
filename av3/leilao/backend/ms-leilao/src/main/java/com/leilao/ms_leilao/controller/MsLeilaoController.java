package com.leilao.ms_leilao.controller;

import com.leilao.ms_leilao.model.Leilao;
import com.leilao.ms_leilao.service.MsLeilaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/leiloes")
public class MsLeilaoController {

    private final MsLeilaoService service;

    public MsLeilaoController(MsLeilaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Leilao> criarLeilao(@RequestBody Leilao leilao) {
        Leilao criado = service.criarLeilao(leilao);
        return ResponseEntity.status(201).body(criado);
    }

    @GetMapping("/ativos")
    public ResponseEntity<Collection<Leilao>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Leilao> buscar(@PathVariable int id) {
        Leilao l = service.buscarPorId(id);
        if (l == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(l);
    }
}
