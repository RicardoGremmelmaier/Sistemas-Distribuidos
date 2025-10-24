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
    public Binding bindingGatewayQueue(Queue gatewayQueue, TopicExchange lanceExchange) {
        // Gateway ouve eventos de lance e pagamento
        return BindingBuilder.bind(gatewayQueue).to(lanceExchange).with("#");
    }
}
