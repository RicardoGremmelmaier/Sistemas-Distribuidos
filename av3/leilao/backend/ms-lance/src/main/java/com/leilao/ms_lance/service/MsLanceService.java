package com.leilao.ms_lance.service;

import com.leilao.ms_lance.messaging.LancePublisher;
import com.leilao.ms_lance.model.EventoLance;
import com.leilao.ms_lance.model.Lance;
import com.leilao.ms_lance.model.ResultadoLance;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MsLanceService {

    private final LanceValidatorService validator;
    private final LancePublisher publisher;

    public MsLanceService(LanceValidatorService validator, LancePublisher publisher) {
        this.validator = validator;
        this.publisher = publisher;
    }

    public ResultadoLance processarLance(Lance lance) {
        ResultadoLance resultado = validator.validar(lance);

        if (resultado.isValido()) {
            EventoLance evento = new EventoLance(
                "lance_validado",
                "Novo lance aceito para leilão " + lance.getLeilaoId(),
                Map.of("leilaoId", lance.getLeilaoId(), "valor", lance.getValor(), "clienteId", lance.getClienteId())
            );
            publisher.publish("lance.validado", evento);
        } else {
            EventoLance evento = new EventoLance(
                "lance_invalidado",
                "Lance invalidado: " + resultado.getMotivo(),
                Map.of("leilaoId", lance.getLeilaoId(), "clienteId", lance.getClienteId())
            );
            publisher.publish("lance.invalidado", evento);
        }
        return resultado;
    }
}
