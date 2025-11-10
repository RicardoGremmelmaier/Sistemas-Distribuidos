package com.leilao.api_gateway.controller;

import com.leilao.api_gateway.model.Lance;
import com.leilao.api_gateway.service.LanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lances")
@CrossOrigin(origins = "*")
public class LanceController {

    @Autowired
    private LanceService lanceService;

    @PostMapping
    public ResponseEntity<String> enviarLance(@RequestBody Lance lance) {
        lanceService.enviarLance(lance);
        return ResponseEntity.ok("Lance enviado com sucesso para o MS Lance");
    }

}
