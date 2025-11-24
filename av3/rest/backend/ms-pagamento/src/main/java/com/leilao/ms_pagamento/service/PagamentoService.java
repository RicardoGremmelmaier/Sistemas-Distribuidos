package com.leilao.ms_pagamento.service;
import com.leilao.ms_pagamento.messaging.PagamentoPublisher;
import com.leilao.ms_pagamento.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@Service
public class PagamentoService {
    
    private final PagamentoPublisher publisher;

    public PagamentoService(PagamentoPublisher publisher) {
        this.publisher = publisher;
    }

    public PagamentoResponse solicitarPagamento (PagamentoRequest request){
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> payload = Map.of(
            "leilaoId", request.getLeilaoId(),
            "valor", request.getValor(),
            "clienteId", request.getClienteId()
        );

        try {
                ResponseEntity<Map> res = restTemplate.postForEntity(
                        "http://localhost:8084/external-payment/create",
                        payload,
                        Map.class
                );

            Map body = res.getBody();
            String link = (String) body.get("linkPagamento");

            PagamentoResponse response = new PagamentoResponse(link, StatusPagamento.PENDENTE);

            EventoPagamento evento = new EventoPagamento(
                "link_pagamento",
                "Link gerado para o pagamento do leilão " + request.getLeilaoId(),
                Map.of(
                        "leilaoId", request.getLeilaoId(),
                        "clienteId", request.getClienteId(),
                        "valor", request.getValor(),
                        "link", link
                )
            );

            publisher.publish("link.pagamento", evento);

            return response;

        } catch (Exception e) {
            System.err.println("[PagamentoService] Erro ao chamar sistema externo: " + e.getMessage());
            throw new RuntimeException("Falha ao solicitar pagamento externo", e);
        }
    }

    public void atualizarStatusPagamento (int leilaoId, StatusPagamento status, int clienteId){
        EventoPagamento evento = new EventoPagamento(
            "status_pagamento",
            "Status atualizado para " + status,
            Map.of(
                    "leilaoId", leilaoId,
                    "status", status.name(),
                    "clienteId", clienteId
            )
        );

        publisher.publish("status.pagamento", evento);
    }
}
