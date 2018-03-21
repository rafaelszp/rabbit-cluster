package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.fanout;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.io.IOException;

/**
 * Created by rafael on 2/13/17.
 */
public class LogXMQOp2Consumer extends AbstractSimpleRMQ {

  private static final String EXCHANGE_NAME = "logs";

  public static void main(String... args) throws Exception {

    initConnection();

    channel.exchangeDeclare(
            EXCHANGE_NAME,
            "fanout" //EXCHANGE TYPE - A fanout exchange routes messages to all of the queues that are bound
            // to it and the routing key is ignored. Ideal for broadcast
    );
    String queueName = "operation-two";
    channel.queueDeclare(queueName, false, false, false, null);

    channel.queueBind(queueName,
            EXCHANGE_NAME,
            ""//ROUTING KEY
    );

    System.out.println("Receiving messages ... ");
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.printf("\nReceived message {%s} for {%s} queue", message, queueName);
      }
    };

    channel.basicConsume(queueName, true, consumer);

  }
}
