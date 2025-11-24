package com.leilao.ms_pagamento.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_LANCE = "lance.events";
    public static final String EXCHANGE_PAGAMENTO = "pagamento.events";
    public static final String QUEUE_PAGAMENTO = "mspagamento.queue";

    @Bean
    public TopicExchange lanceExchange() {
        return new TopicExchange(EXCHANGE_LANCE);
    }

    @Bean
    public TopicExchange pagamentoExchange() {
        return new TopicExchange(EXCHANGE_PAGAMENTO);
    }

    @Bean
    public Queue pagamentoQueue() {
        return new Queue(QUEUE_PAGAMENTO, true);
    }

    @Bean
    public Binding bindPagamentoQueue(Queue pagamentoQueue, TopicExchange lanceExchange) {
        return BindingBuilder.bind(pagamentoQueue).to(lanceExchange).with("leilao.vencedor");
    }
}