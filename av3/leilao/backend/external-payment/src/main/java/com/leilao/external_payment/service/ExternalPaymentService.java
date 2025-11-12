package com.leilao.external_payment.service;

import com.leilao.external_payment.model.PaymentTransaction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExternalPaymentService {

    private final Map<String, PaymentTransaction> transactions = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> createPayment(Map<String, Object> payload) {
        int leilaoId = (int) payload.get("leilaoId");
        double valor = ((Number) payload.get("valor")).doubleValue();
        Map<?, ?> dadosCliente = (Map<?, ?>) payload.get("dadosCliente");

        String idTransacao = UUID.randomUUID().toString();
        String linkPagamento = "http://localhost:3000/external-payment/" + idTransacao;

        PaymentTransaction transaction = new PaymentTransaction(idTransacao, leilaoId, valor, "PENDENTE", dadosCliente);
        transactions.put(idTransacao, transaction);

        System.out.println("[MockPagamento] Transação criada: " + transaction);

        return Map.of(
                "idTransacao", idTransacao,
                "linkPagamento", linkPagamento,
                "status", "PENDENTE"
        );
    }

    public void finalizarPagamento(Map<String, Object> payload) {
        String idTransacao = (String) payload.get("idTransacao");
        String status = (String) payload.get("status");

        PaymentTransaction transaction = transactions.get(idTransacao);
        if (transaction == null) throw new RuntimeException("Transação não encontrada");

        transaction.setStatus(status);

        Map<String, Object> callbackPayload = Map.of(
                "idTransacao", transaction.getIdTransacao(),
                "leilaoId", transaction.getLeilaoId(),
                "valor", transaction.getValor(),
                "status", transaction.getStatus(),
                "dadosCliente", transaction.getDadosCliente()
        );

        System.out.println("[MockPagamento] Enviando callback: " + callbackPayload);
        restTemplate.postForEntity("http://localhost:8083/pagamento/callback", callbackPayload, Void.class);
    }
}
