package com.leilao.api_gateway.service;

import com.leilao.api_gateway.model.Evento;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GatewaySubscriber {

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = "queue.gateway")
    public void consumirEvento(Evento evento) {
        // Lógica para processar a notificação recebida
        // Mapear evento -> cliente(s) e enviar notificação
        System.out.println("Notificação recebida: " + evento);
        notificationService.notificarCliente(1, evento);
    }

}
