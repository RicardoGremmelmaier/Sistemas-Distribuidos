package com.leilao.ms_pagamento.messaging;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.leilao.ms_pagamento.config.RabbitConfig;
import com.leilao.ms_pagamento.model.EventoPagamento;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PagamentoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public PagamentoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String routingKey, EventoPagamento evento) {
        try {
            String json = mapper.writeValueAsString(evento);
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_PAGAMENTO, routingKey, json);
            System.out.println("[MSPagamento] Publicado: " + routingKey + " -> " + json);
        } catch (Exception e) {
            System.out.println(" [MSPagamento] Erro ao publicar: " + routingKey + " -> " + e.getMessage());
        }
    }
    
}