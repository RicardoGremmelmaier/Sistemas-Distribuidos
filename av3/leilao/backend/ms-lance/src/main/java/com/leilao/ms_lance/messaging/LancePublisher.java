package com.leilao.ms_lance.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leilao.ms_lance.config.RabbitConfig;
import com.leilao.ms_lance.model.EventoLance;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class LancePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public LancePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String routingKey, EventoLance evento) {
        try {
            String payload = mapper.writeValueAsString(evento);
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_LEILAO, routingKey, payload);
            System.out.println("Evento publicado: " + routingKey + " -> " + payload);
        } catch (Exception e) {
            System.err.println("Erro ao publicar lance: " + e.getMessage());
        }
    }
}
