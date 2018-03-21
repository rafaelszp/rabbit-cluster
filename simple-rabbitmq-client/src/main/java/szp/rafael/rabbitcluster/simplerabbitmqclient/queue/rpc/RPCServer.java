package szp.rafael.rabbitcluster.simplerabbitmqclient.queue.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by rafael on 2/14/17.
 */
public class RPCServer extends AbstractSimpleRMQ {

  private static final String QUEUE_NAME = "rpc-queue";


  public static void main(String... args) throws IOException, TimeoutException {

    initConnection();
    boolean durable = true;
    channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
    channel.basicQos(1);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {

        AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                .Builder()
                .correlationId(properties.getCorrelationId())
                .build();
        String response = "";

        try {
          String message = new String(body, "UTF-8");
          int number = Integer.parseInt(message);
          System.out.printf("Calculating fib(%s)\n", number);
          response += String.format("fib(%s) = ", number) + fib(number);
        } catch (RuntimeException e) {
          e.printStackTrace();
        } finally {
          channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
          channel.basicAck(envelope.getDeliveryTag(), false); // acknowledge receipt of the message
        }

      }

    };
    channel.basicConsume(QUEUE_NAME,
            false, //autoack. Will be manually checked in handleDelivery above
            consumer
    );
  }

  private static int fib(int n) {
    if (n == 0) return 0;
    if (n == 1) return 1;
    return fib(n - 1) + fib(n - 2);
  }

}
