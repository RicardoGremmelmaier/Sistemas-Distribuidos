package com.leilao.api_gateway.controller;

import com.leilao.api_gateway.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notificacoes")
@CrossOrigin(origins = "*")
public class NotificacaoController {

    private final NotificationService service;

    public NotificacaoController(NotificationService service) {
        this.service = service;
    }

    @GetMapping("/subscribe/{leilaoId}/{clienteId}")
    public SseEmitter subscribe(@PathVariable Integer leilaoId, @PathVariable Integer clienteId) {
        return service.registrarClienteEmLeilao(leilaoId, clienteId);
    }

    @DeleteMapping("/unsubscribe/{leilaoId}/{clienteId}")
    public void unsubscribe(@PathVariable Integer leilaoId, @PathVariable Integer clienteId) {
        service.cancelarInscricaoCliente(leilaoId, clienteId);
    }

    @DeleteMapping("/cancelar/{leilaoId}")
    public void cancelar(@PathVariable Integer leilaoId) {
        service.cancelarNotificacoesDeLeilao(leilaoId);
    }

}
