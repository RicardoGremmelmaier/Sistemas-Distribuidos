package com.leilao.api_gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leilao.api_gateway.model.Evento;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GatewaySubscriber {

    @Autowired
    private NotificationService notificationService;

    private final ObjectMapper mapper = new ObjectMapper();

    @RabbitListener(queues = "queue.gateway")
    public void consumirEvento(String mensagem) {
        try {
            Evento evento = mapper.readValue(mensagem, Evento.class);
            System.out.println("[Gateway] Notificação recebida: " + evento);
            notificationService.notificarCliente(1, evento);
        } catch (Exception e) {
            System.err.println("[Gateway] Erro ao processar mensagem: " + e.getMessage());
            System.err.println("Conteúdo bruto: " + mensagem);
        }
    }
}