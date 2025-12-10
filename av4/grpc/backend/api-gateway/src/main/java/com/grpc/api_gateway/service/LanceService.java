package com.grpc.api_gateway.service;

import com.grpc.api_gateway.model.Lance;
import com.grpc.common.grpc.lance.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class LanceService {

    @GrpcClient("lance-client")
    private LanceServiceGrpc.LanceServiceBlockingStub lanceStub;

    public void enviarLance(Lance lance) {
        LanceRequest request = LanceRequest.newBuilder()
                .setLeilaoId(lance.getLeilaoId())
                .setClienteId(lance.getClienteId())
                .setValor(lance.getValor())
                .build();

        LanceResponse response = lanceStub.processarLance(request);

        if (!response.getValido()) {
            throw new RuntimeException("Lance rejeitado: " + response.getMotivo());
        }
    }

    public Double getMaiorLance(int leilaoId) {
        VencedorResponse response = lanceStub.obterVencedor(
            LeilaoIdRequest.newBuilder().setId(leilaoId).build()
        );
        return response.getValor();
    }
}