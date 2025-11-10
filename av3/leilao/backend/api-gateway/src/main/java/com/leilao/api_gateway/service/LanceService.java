package com.leilao.api_gateway.service;

import com.leilao.api_gateway.model.Lance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LanceService {

    private final WebClient webClient;

    public LanceService(@Value("${services.lance}") String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public void enviarLance(Lance lance) {
        webClient.post()
                .uri("/lances")
                .bodyValue(lance)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    public Double getMaiorLance(int leilaoId) {
        return webClient.get()
                .uri("/lances/maior/{leilaoId}", leilaoId)
                .retrieve()
                .bodyToMono(Double.class)
                .block();
    }
    
}
