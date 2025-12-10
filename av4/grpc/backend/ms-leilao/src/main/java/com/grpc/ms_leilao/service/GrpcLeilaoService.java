package com.grpc.ms_leilao.service;

import com.leilao.common.grpc.leilao.*;
import com.grpc.ms_leilao.model.Leilao;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@GrpcService
public class GrpcLeilaoService extends LeilaoServiceGrpc.LeilaoServiceImplBase {

    @Autowired
    private MsLeilaoService leilaoService;

    @Override
    public void criarLeilao(CriarLeilaoRequest request, StreamObserver<LeilaoDto> responseObserver) {
        Leilao novo = new Leilao();
        novo.setNomeDoProduto(request.getNomeProduto());
        novo.setLanceInicial(request.getLanceInicial());
        
        if (!request.getDataInicio().isEmpty()) 
            novo.setDataInicio(LocalDateTime.parse(request.getDataInicio()));
        if (!request.getDataFim().isEmpty()) 
            novo.setDataFim(LocalDateTime.parse(request.getDataFim()));

        Leilao criado = leilaoService.criarLeilao(novo);

        responseObserver.onNext(toDto(criado));
        responseObserver.onCompleted();
    }

    @Override
    public void listarLeiloes(Empty request, StreamObserver<ListaLeiloes> responseObserver) {
        ListaLeiloes.Builder builder = ListaLeiloes.newBuilder();
        
        leilaoService.listarAtivos().forEach(l -> builder.addLeiloes(toDto(l)));

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void buscarLeilao(LeilaoIdRequest request, StreamObserver<LeilaoDto> responseObserver) {
        Leilao l = leilaoService.buscarPorId(request.getId());
        if (l != null) {
            responseObserver.onNext(toDto(l));
        } else {
            // gRPC error handling simples
            responseObserver.onNext(LeilaoDto.getDefaultInstance()); 
        }
        responseObserver.onCompleted();
    }

    // Auxiliar para converter Model -> Proto DTO
    private LeilaoDto toDto(Leilao l) {
        return LeilaoDto.newBuilder()
                .setId(l.getId())
                .setNomeProduto(l.getNomeDoProduto() != null ? l.getNomeDoProduto() : "")
                .setStatus(l.getStatus() != null ? l.getStatus().name() : "")
                .setLanceInicial(l.getLanceInicial())
                .setDataInicio(l.getDataInicio() != null ? l.getDataInicio().toString() : "")
                .setDataFim(l.getDataFim() != null ? l.getDataFim().toString() : "")
                .build();
    }
}