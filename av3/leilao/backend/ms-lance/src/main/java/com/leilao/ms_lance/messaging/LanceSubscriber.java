package com.leilao.ms_lance.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leilao.ms_lance.model.EventoLance;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LanceSubscriber {

    private final ObjectMapper mapper = new ObjectMapper();

    @RabbitListener(queues = "mslance.queue")
    public void receberMensagem(String mensagem) {
        try {
            EventoLance evento = mapper.readValue(mensagem, EventoLance.class);
            System.out.println("[MSLance] recebeu evento: " + evento.getTipo());
            // TODO: reagir a leilao_iniciado, leilao_finalizado, etc.
        } catch (Exception e) {
            System.err.println("[MSLance] Erro ao processar evento: " + e.getMessage());
        }
    }
}
