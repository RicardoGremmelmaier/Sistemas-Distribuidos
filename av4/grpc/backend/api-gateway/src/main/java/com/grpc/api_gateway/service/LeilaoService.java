package com.grpc.api_gateway.service;

import com.grpc.api_gateway.model.Leilao;
import com.leilao.common.grpc.leilao.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeilaoService {

    @GrpcClient("leilao-client")
    private LeilaoServiceGrpc.LeilaoServiceBlockingStub leilaoStub;

    public void criarLeilao(Leilao leilao) {
        CriarLeilaoRequest request = CriarLeilaoRequest.newBuilder()
                .setNomeProduto(leilao.getNomeDoProduto())
                .setLanceInicial(leilao.getLanceInicial())
                .setDataInicio(leilao.getDataInicio().toString())
                .setDataFim(leilao.getDataFim().toString())
                .build();

        // Chamada gRPC Síncrona
        leilaoStub.criarLeilao(request);
    }

    public List<Leilao> listarLeiloes() {
        // Chamada gRPC
        ListaLeiloes listaProto = leilaoStub.listarLeiloes(Empty.newBuilder().build());

        // Converte de Proto para Objeto Java do Gateway
        List<Leilao> leiloes = new ArrayList<>();
        for (LeilaoDto dto : listaProto.getLeiloesList()) {
            leiloes.add(mapDtoToModel(dto));
        }
        return leiloes;
    }

    public Leilao getLeilao(int leilaoId) {
        LeilaoDto dto = leilaoStub.buscarLeilao(LeilaoIdRequest.newBuilder().setId(leilaoId).build());
        return mapDtoToModel(dto);
    }

    private Leilao mapDtoToModel(LeilaoDto dto) {
        Leilao l = new Leilao();
        l.setLeilaoId(dto.getId());
        l.setNomeDoProduto(dto.getNomeProduto());
        l.setLanceInicial(dto.getLanceInicial());
        return l;
    }
}