package com.grpc.api_gateway.controller;

import com.grpc.api_gateway.service.NotificationService;
import com.grpc.api_gateway.model.Evento;

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

    @GetMapping("/inscrito/{leilaoId}/{clienteId}")
        public boolean isInscrito(@PathVariable Integer leilaoId, @PathVariable Integer clienteId) {
        return service.isInscrito(leilaoId, clienteId);
    }

    @PostMapping("/notificar/{leilaoId}/{clienteId}")
    public void notificarCliente(@PathVariable Integer leilaoId, @PathVariable Integer clienteId, @RequestBody Evento evento) {
        service.notificarCliente(leilaoId, clienteId, evento);
    }

    @PostMapping("/notificar/{leilaoId}")
    public void notificarLeilao(@PathVariable Integer leilaoId, @RequestBody Evento evento) {
        service.notificarLeilao(leilaoId, evento);
    }

}
