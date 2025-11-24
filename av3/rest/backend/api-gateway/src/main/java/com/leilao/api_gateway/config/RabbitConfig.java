package com.leilao.api_gateway.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_LEILAO = "leilao.events";
    public static final String EXCHANGE_LANCE = "lance.events";
    public static final String EXCHANGE_PAGAMENTO = "pagamento.events";

    public static final String QUEUE_GATEWAY = "queue.gateway";

    @Bean
    public TopicExchange leilaoExchange() {
        return new TopicExchange(EXCHANGE_LEILAO);
    }

    @Bean
    public TopicExchange lanceExchange() {
        return new TopicExchange(EXCHANGE_LANCE);
    }

    @Bean
    public TopicExchange pagamentoExchange() {
        return new TopicExchange(EXCHANGE_PAGAMENTO);
    }

    @Bean
    public Queue gatewayQueue() {
        return new Queue(QUEUE_GATEWAY, true);
    }

    @Bean
    public Binding bindGatewayQueueToLeilao(Queue gatewayQueue, TopicExchange leilaoExchange) {
        return BindingBuilder.bind(gatewayQueue).to(leilaoExchange).with("#");
    }

    @Bean
    public Binding bindGatewayQueueToLance(Queue gatewayQueue, TopicExchange lanceExchange) {
        return BindingBuilder.bind(gatewayQueue).to(lanceExchange).with("#");
    }

    @Bean
    public Binding bindGatewayQueueToPagamento(Queue gatewayQueue, TopicExchange pagamentoExchange) {
        return BindingBuilder.bind(gatewayQueue).to(pagamentoExchange).with("#");
    }
}
