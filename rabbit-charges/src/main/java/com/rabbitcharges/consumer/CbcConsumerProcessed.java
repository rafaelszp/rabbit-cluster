package com.rabbitcharges.consumer;

import com.rabbitmq.client.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeoutException;

/**
 * Created by igor on 2/22/17.
 */
public class CbcConsumerProcessed {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory;
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        try {
            Files.newBufferedWriter(Paths.get("processed.log"), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
        }
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                Path path = Paths.get("processed.log");
                try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                    writer.write(message);
                    writer.newLine();
                }
                getChannel().basicAck(envelope.getDeliveryTag(),true);
            }
        };
        channel.basicConsume("q_cbc_processed",false,consumer);
    }

}
