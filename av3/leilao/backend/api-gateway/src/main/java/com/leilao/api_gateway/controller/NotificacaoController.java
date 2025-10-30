package com.leilao.api_gateway.controller;

import com.leilao.api_gateway.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{clienteId}")
    public SseEmitter registrarNotificacao(@PathVariable Integer clienteId) {
        return notificationService.registrarCliente(clienteId);
    }

    @DeleteMapping("/{clienteId}")
    public void removerNotificacao(@PathVariable Integer clienteId) {
        notificationService.cancelarNotificacao(clienteId);
    }

}
