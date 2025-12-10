package com.grpc.ms_pagamento.service;

import com.leilao.common.grpc.pagamento.PagamentoRequestProto;
import com.leilao.common.grpc.pagamento.PagamentoResponseProto;
import com.leilao.common.grpc.pagamento.PagamentoServiceGrpc;
import com.grpc.ms_pagamento.model.PagamentoRequest;
import com.grpc.ms_pagamento.model.PagamentoResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class GrpcPagamentoService extends PagamentoServiceGrpc.PagamentoServiceImplBase {

    @Autowired
    private PagamentoService pagamentoService;

    @Override
    public void solicitarPagamento(PagamentoRequestProto request, StreamObserver<PagamentoResponseProto> responseObserver) {
        // Converte do Proto para o Model interno
        PagamentoRequest modelRequest = new PagamentoRequest();
        modelRequest.setLeilaoId(request.getLeilaoId());
        modelRequest.setClienteId(request.getClienteId());
        modelRequest.setValor(request.getValor());

        // Chama a lógica de negócio (que vai no sistema externo via REST)
        PagamentoResponse modelResponse = pagamentoService.solicitarPagamento(modelRequest);

        // Devolve a resposta via gRPC para o ms-leilao
        PagamentoResponseProto responseProto = PagamentoResponseProto.newBuilder()
                .setLinkPagamento(modelResponse.getLinkPagamento())
                .setStatus(modelResponse.getStatus().name())
                .build();

        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }
}