package com.leilao.ms_pagamento.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leilao.ms_pagamento.model.PagamentoRequest;
import com.leilao.ms_pagamento.service.PagamentoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class PagamentoSubscriber {

    private final ObjectMapper mapper = new ObjectMapper();
    private final PagamentoService pagamentoService;

    public PagamentoSubscriber(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @RabbitListener(queues = "mspagamento.queue")
    public void receberMensagem(String mensagem) {
        try {
            Map<?, ?> event = mapper.readValue(mensagem, Map.class);
            if ("leilao_vencedor".equals(event.get("tipo"))) {
                Map<?, ?> dados = (Map<?, ?>) event.get("dados");

                int leilaoId = (int) dados.get("leilaoId");
                int clienteId = (int) dados.get("clienteId");
                double valor = ((Number) dados.get("valor")).doubleValue();

                pagamentoService.solicitarPagamento(new PagamentoRequest(leilaoId, clienteId, valor));
            }
        } catch (Exception e) {
            System.err.println("[MSPagamento] Erro ao processar mensagem: " + e.getMessage());
        }
    }
}
