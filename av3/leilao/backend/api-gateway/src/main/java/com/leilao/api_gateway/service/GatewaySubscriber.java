package com.leilao.api_gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leilao.api_gateway.model.Evento;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class GatewaySubscriber {

    private final NotificationService notificationService;
    private final ObjectMapper mapper = new ObjectMapper();

    public GatewaySubscriber(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "queue.gateway")
    public void consumirEvento(String mensagem) {
        try {
            Evento evento = mapper.readValue(mensagem, Evento.class);
            System.out.println("[Gateway] Notificação recebida: " + evento);

            @SuppressWarnings("unchecked")
            Map<String, Object> dados = (Map<String, Object>) evento.getDados();

            Object leilaoIdObj = dados.get("leilaoId");
            if (leilaoIdObj == null) {
                System.err.println("[Gateway] Evento sem leilaoId — ignorado: " + evento.getTipo());
                return;
            }

            Integer leilaoId = (leilaoIdObj instanceof Integer)
                    ? (Integer) leilaoIdObj
                    : Integer.parseInt(leilaoIdObj.toString());

            notificationService.notificarLeilao(leilaoId, evento);

        } catch (Exception e) {
            System.err.println("[Gateway] Erro ao processar mensagem: " + e.getMessage());
            System.err.println("Conteúdo bruto: " + mensagem);
        }
    }
}
