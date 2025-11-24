package com.leilao.ms_leilao.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_LEILAO = "leilao.events";

    @Bean
    public TopicExchange leilaoExchange() {
        return new TopicExchange(EXCHANGE_LEILAO);
    }
}
