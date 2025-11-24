package com.leilao.external_payment.controller;

import com.leilao.external_payment.service.ExternalPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/external-payment")
public class ExternalPaymentController {

    private final ExternalPaymentService paymentService;

    public ExternalPaymentController(ExternalPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(paymentService.createPayment(payload));
    }

    @PostMapping("/finalizar")
    public ResponseEntity<String> finalizarPagamento(@RequestBody Map<String, Object> payload) {
        try {
            paymentService.finalizarPagamento(payload);
            return ResponseEntity.ok("Callback enviado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }
}
