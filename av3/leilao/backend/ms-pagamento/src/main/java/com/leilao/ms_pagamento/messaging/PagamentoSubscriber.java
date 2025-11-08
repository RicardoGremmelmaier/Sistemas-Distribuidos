package com.leilao.ms_pagamento.messaging;

import com.leilao.ms_pagamento.model.Evento;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leilao.ms_pagamento.model.DadosVencedor;
import com.leilao.ms_pagamento.model.PagamentoRequest;
import com.leilao.ms_pagamento.service.PagamentoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoSubscriber {

    private final ObjectMapper mapper = new ObjectMapper();
    private final PagamentoService pagamentoService;

    public PagamentoSubscriber(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @RabbitListener(queues = "mspagamento.queue")
    public void receberMensagem(String mensagem) {
        try {
            Evento evento = mapper.readValue(mensagem, Evento.class);
            if ("leilao_vencedor".equals(evento.getTipo())) {
                DadosVencedor dados = mapper.convertValue(evento.getDados(), DadosVencedor.class);

                System.out.println("[MSPagamento] Processando pagamento para leilão " + dados.getLeilaoId());
                System.out.println("Vencedor: " + dados.getVencedorId() + " | Valor: " + dados.getValor());


                pagamentoService.solicitarPagamento(new PagamentoRequest(dados.getLeilaoId(), dados.getVencedorId(), dados.getValor()));
            }
        } catch (Exception e) {
            System.err.println("[MSPagamento] Erro ao processar mensagem: " + e.getMessage());
        }
    }
}
