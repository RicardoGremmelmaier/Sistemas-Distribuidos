package com.grpc.api_gateway.controller;

import com.grpc.api_gateway.model.Lance;
import com.grpc.api_gateway.service.LanceService;
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

    @GetMapping("/maior/{leilaoId}")
    public ResponseEntity<Double> getMaiorLance(@PathVariable int leilaoId) {
        Double maiorLance = lanceService.getMaiorLance(leilaoId);
        if (maiorLance != null) {
            return ResponseEntity.ok(maiorLance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
