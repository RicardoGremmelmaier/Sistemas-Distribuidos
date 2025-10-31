package com.leilao.ms_leilao.messaging;

import com.leilao.ms_leilao.config.RabbitConfig;
import com.leilao.ms_leilao.model.EventoLeilao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class LeilaoPublisher {

    
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public LeilaoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String routingKey, EventoLeilao evento) {
        try {
            String payload = mapper.writeValueAsString(evento);
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_LEILAO, routingKey, payload);
            System.out.println("Publicado evento no RabbitMQ: " + routingKey + " -> " + payload);
        } catch (Exception e) {
            System.err.println("Erro ao publicar evento de leilao: " + e.getMessage());
        }
    }
}
