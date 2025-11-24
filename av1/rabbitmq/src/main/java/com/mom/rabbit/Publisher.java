package com.mom.rabbit;

import java.io.IOException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publisher {
    private Connection connection;
    private Channel channel;
    private String exchangeName = "leilao";

    public Publisher() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
        this.channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
    }

    public void publish(String routingKey, String message) throws IOException {
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        System.out.println("--------------------------------------------------");
        System.out.println("[PUB] Enviado '" + routingKey + "':'" + message + "'");
        System.out.println("--------------------------------------------------");
    }
}