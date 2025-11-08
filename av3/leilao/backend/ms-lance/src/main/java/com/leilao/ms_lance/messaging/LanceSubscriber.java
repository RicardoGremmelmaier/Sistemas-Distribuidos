package com.leilao.ms_lance.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leilao.ms_lance.model.EventoLance;
import com.leilao.ms_lance.service.LanceValidatorService;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LanceSubscriber {

    private final ObjectMapper mapper = new ObjectMapper();
    private final LanceValidatorService validator = new LanceValidatorService();

    @RabbitListener(queues = "lance.queue")
    public void receberMensagem(String mensagem) {
        try {
            EventoLance evento = mapper.readValue(mensagem, EventoLance.class);
            System.out.println("[MSLance] recebeu evento: " + evento.getTipo());

            if ("leilao_iniciado".equals(evento.getTipo())) {
                int leilaoId = (int) ((Map<?, ?>) evento.getDados()).get("leilaoId");
                validator.ativarLeilao(leilaoId);
            } else if ("leilao_finalizado".equals(evento.getTipo())) {
                int leilaoId = (int) ((Map<?, ?>) evento.getDados()).get("leilaoId");
                validator.finalizarLeilao(leilaoId);
            }
        } catch (Exception e) {
            System.err.println("[MSLance] Erro ao processar evento: " + e.getMessage());
        }
    }
}
