package com.leilao.ms_lance.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leilao.ms_lance.model.EventoLance;
import com.leilao.ms_lance.service.LanceValidatorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LanceSubscriber {

    private final ObjectMapper mapper = new ObjectMapper();
    private final LanceValidatorService validator;
    private final LancePublisher publisher;

    public LanceSubscriber(LanceValidatorService validator, LancePublisher publisher) {
        this.validator = validator;
        this.publisher = publisher;
    }

    @RabbitListener(queues = "lance.queue")
    public void receberMensagem(String mensagem) {
        try {
            EventoLance evento = mapper.readValue(mensagem, EventoLance.class);
            System.out.println("[MSLance] recebeu evento: " + evento.getTipo());

            if ("leilao_iniciado".equals(evento.getTipo())) {
                int leilaoId = (int) ((Map<?, ?>) evento.getDados()).get("leilaoId");
                validator.ativarLeilao(leilaoId);
            }

            else if ("leilao_finalizado".equals(evento.getTipo())) {
                int leilaoId = (int) ((Map<?, ?>) evento.getDados()).get("leilaoId");
                validator.finalizarLeilao(leilaoId);

                Double valor = validator.getMaiorLance(leilaoId);
                Integer clienteId = validator.getVencedor(leilaoId);

                if (valor != null && valor > 0 && clienteId != null) {
                    EventoLance eventoVencedor = new EventoLance(
                        "leilao_vencedor",
                        "Leilão " + leilaoId + " finalizado. Vencedor: cliente " + clienteId,
                        Map.of("leilaoId", leilaoId, "clienteId", clienteId, "valor", valor)
                    );
                    publisher.publish("leilao.vencedor", eventoVencedor);
                    System.out.println("[MSLance] Evento publicado: leilao_vencedor -> " + eventoVencedor);
                } else {
                    System.out.println("[MSLance] Nenhum lance válido para o leilão " + leilaoId);
                }
            }

        } catch (Exception e) {
            System.err.println("[MSLance] Erro ao processar evento: " + e.getMessage());
        }
    }
}
