package com.leilao.ms_lance.controller;

import com.leilao.ms_lance.model.Lance;
import com.leilao.ms_lance.model.ResultadoLance;
import com.leilao.ms_lance.service.MsLanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lances")
public class MsLanceController {

    private final MsLanceService service;

    public MsLanceController(MsLanceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResultadoLance> enviarLance(@RequestBody Lance lance) {
        ResultadoLance resultado = service.processarLance(lance);
        if (resultado.isValido())
            return ResponseEntity.ok(resultado);
        else
            return ResponseEntity.badRequest().body(resultado);
    }
}
