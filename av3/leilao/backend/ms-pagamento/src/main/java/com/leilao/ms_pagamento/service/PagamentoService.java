package com.leilao.ms_pagamento.service;

import com.leilao.ms_pagamento.messaging.PagamentoPublisher;
import com.leilao.ms_pagamento.model.*;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;

@Service
public class PagamentoService {
    
    private final PagamentoPublisher publisher;

    public PagamentoService(PagamentoPublisher publisher) {
        this.publisher = publisher;
    }

    public PagamentoResponse solicitarPagamento (PagamentoRequest request){
        String fakeLink = "https://pagamentos.externo.com/pagar/" + UUID.randomUUID();

        PagamentoResponse response = new PagamentoResponse(fakeLink, StatusPagamento.PENDENTE);

        EventoPagamento evento = new EventoPagamento(
            "link_pagamento",
            "Link gerado para o pagamento do leilão " + request.getIdLeilao(),
            Map.of(
                    "idLeilao", request.getIdLeilao(),
                    "idCliente", request.getIdCliente(),
                    "valor", request.getValor(),
                    "link", fakeLink
            )
        );

        publisher.publish("link.pagamento", evento);

        return response;
    }

    public void atualizarStatusPagamento (int idLeilao, StatusPagamento status){
        EventoPagamento evento = new EventoPagamento(
            "status_pagamento",
            "Status atualizado para " + status,
            Map.of(
                    "idLeilao", idLeilao,
                    "status", status.name()
            )
        );

        publisher.publish("status.pagamento", evento);
    }
}
