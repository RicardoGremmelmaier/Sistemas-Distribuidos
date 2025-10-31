package com.leilao.ms_lance.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_LEILAO = "leilao.events";
    public static final String QUEUE_LANCES = "lance.queue";

    @Bean
    public TopicExchange leilaoExchange() {
        return new TopicExchange(EXCHANGE_LEILAO);
    }

    @Bean
    public Queue lanceQueue() {
        return new Queue(QUEUE_LANCES);
    }

    @Bean
    public Binding lanceBinding(TopicExchange leilaoExchange, Queue lanceQueue) {
        return BindingBuilder.bind(lanceQueue).to(leilaoExchange).with("leilao.*");
    }

}
