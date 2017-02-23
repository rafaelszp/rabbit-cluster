package com.rabbitcharges.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by igor on 2/22/17.
 */
public class CbcConsumerGoias {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory;
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                System.out.printf("%s\n",message);
                getChannel().basicAck(envelope.getDeliveryTag(),true);
                getChannel().basicPublish("","q_cbc_processed", null, body);
            }
        };
        channel.basicConsume("q_cbc_goias",false,consumer);
    }

}
