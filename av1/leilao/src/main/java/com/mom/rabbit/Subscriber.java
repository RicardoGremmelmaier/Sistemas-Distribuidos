package com.mom.rabbit;

import java.util.List;
import java.util.function.Consumer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Subscriber {
    private Connection connection;
    private Channel channel;
    private String exchangeName = "leilao";
    private String queueName;
    
    public Subscriber(List<String> routingKeys, Consumer<String> handler) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
        this.channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        this.queueName = channel.queueDeclare().getQueue();

        for (String key : routingKeys) {
            channel.queueBind(queueName, exchangeName, key);
        }

        channel.basicConsume(queueName, true, (tag, delivery) -> {
            String msg = new String(delivery.getBody(), "UTF-8");
            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println("[SUB] Recebido [" + routingKey + "]: " + msg);
            handler.accept("[" + routingKey + "] " + msg);
        }, tag -> {});
    }

    public void queueBind(String routingKey) throws Exception {
        channel.queueBind(queueName, exchangeName, routingKey);
    }

}