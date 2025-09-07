package com.mom.rabbit;

import java.io.IOException;
import java.util.List;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class AbstractSubscriber {
    protected Channel channel;
    protected String exchangeName = "leilao";
    public AbstractSubscriber(List<String> routingKeys) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        this.channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, "direct", true);
        String queueName = channel.queueDeclare().getQueue();

        for (String key : routingKeys) {
            channel.queueBind(queueName, exchangeName, key);
        }

        channel.basicConsume(queueName, true, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[SUB] Recebido '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            handleMessage(message);
        }, consumerTag -> {});
    }

    protected abstract void handleMessage(String message);
}