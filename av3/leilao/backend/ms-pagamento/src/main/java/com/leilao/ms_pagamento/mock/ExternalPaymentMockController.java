package com.leilao.ms_pagamento.mock;

import com.leilao.ms_pagamento.model.StatusPagamento;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/external-payment")
@EnableAsync
@Component
public class ExternalPaymentMockController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody Map<String, Object> payload) {
        int leilaoId = (int) payload.get("leilaoId");
        double valor = ((Number) payload.get("valor")).doubleValue();
        Map<?, ?> dadosCliente = (Map<?, ?>) payload.get("dadosCliente");

        String idTransacao = UUID.randomUUID().toString();
        String fakeLink = "https://pagamento.mock.com/pay/" + idTransacao;

        System.out.println("[MockPagamento] Nova transação criada:");
        System.out.println("  • ID Transação: " + idTransacao);
        System.out.println("  • Leilão: " + leilaoId);
        System.out.println("  • Valor: R$ " + valor);

        simulateAsyncCallback(idTransacao, leilaoId, valor, dadosCliente);

        Map<String, Object> response = Map.of(
                "idTransacao", idTransacao,
                "linkPagamento", fakeLink,
                "status", "PENDENTE",
                "mensagem", "Link gerado com sucesso"
        );

        return ResponseEntity.ok(response);
    }

    @Async
    public void simulateAsyncCallback(String idTransacao, int leilaoId, double valor, Map<?, ?> dadosCliente) {
        try {
            int delay = ThreadLocalRandom.current().nextInt(5000, 10000);
            Thread.sleep(delay);

            StatusPagamento status = ThreadLocalRandom.current().nextBoolean()
                    ? StatusPagamento.APROVADO
                    : StatusPagamento.RECUSADO;

            Map<String, Object> callbackPayload = Map.of(
                    "idTransacao", idTransacao,
                    "leilaoId", leilaoId,
                    "valor", valor,
                    "status", status.name(),
                    "dadosCliente", dadosCliente
            );

            System.out.println("[MockPagamento] Enviando callback: " + callbackPayload);
            restTemplate.postForEntity("http://localhost:8083/pagamento/callback", callbackPayload, Void.class);

        } catch (Exception e) {
            System.err.println("[MockPagamento] Erro ao simular callback: " + e.getMessage());
        }
    }
}
