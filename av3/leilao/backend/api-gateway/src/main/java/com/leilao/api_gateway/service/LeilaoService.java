package com.leilao.api_gateway.service;

import com.leilao.api_gateway.model.Leilao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LeilaoService {

    private final WebClient webClient;

    public LeilaoService(@Value("${services.leilao}") String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public void criarLeilao(Leilao leilao) {
        webClient.post()
                .uri("/leiloes")
                .bodyValue(leilao)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    public Object listarLeiloes(){
        return webClient.get()
                .uri("/leiloes/ativos")
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(e -> Mono.just("Erro ao consultar leilões: " + e.getMessage()))
                .block();
    }

}
