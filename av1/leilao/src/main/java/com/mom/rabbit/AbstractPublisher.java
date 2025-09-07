package com.mom.rabbit;

import java.io.IOException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class AbstractPublisher {
    protected Channel channel;
    protected String exchangeName = "leilao";

    public AbstractPublisher() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        }
    }

    protected void publish(String routingKey, String message) throws IOException {
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        System.out.println("[PUB] Enviado '" + routingKey + "':'" + message + "'");
    }
}