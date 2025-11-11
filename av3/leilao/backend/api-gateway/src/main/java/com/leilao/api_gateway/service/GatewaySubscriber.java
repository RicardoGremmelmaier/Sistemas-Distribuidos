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

            Integer leilaoId = Integer.parseInt(dados.get("leilaoId").toString());
            Integer clienteId = dados.get("clienteId") != null
                    ? Integer.parseInt(dados.get("clienteId").toString())
                    : null;

            switch (evento.getTipo()) {
                //Tipos que notificam todos os inscritos no leilão
                case "leilao_finalizado":
                case "lance_validado":
                    notificationService.notificarLeilao(leilaoId, evento);
                    System.out.println("[Gateway] Notificação enviada para o leilão " + leilaoId);
                    break;

                //Tipos que notificam um cliente específico
                case "lance_invalidado":
                case "leilao_vencedor":
                case "link_pagamento":
                case "status_pagamento":
                    if (clienteId != null) {
                        notificationService.notificarCliente(leilaoId, clienteId, evento);
                        System.out.println("[Gateway] Notificação enviada ao cliente " + clienteId);
                    }
                    break;

                default:
                    notificationService.notificarLeilao(leilaoId, evento);
            }

        } catch (Exception e) {
            System.err.println("[Gateway] Erro ao processar mensagem: " + e.getMessage());
            System.err.println("Conteúdo bruto: " + mensagem);
        }
    }
}
