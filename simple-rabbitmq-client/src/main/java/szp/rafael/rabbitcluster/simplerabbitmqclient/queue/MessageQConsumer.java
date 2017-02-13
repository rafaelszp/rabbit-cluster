package szp.rafael.rabbitcluster.simplerabbitmqclient.queue;

import com.rabbitmq.client.*;
import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by rafael on 2/10/17.
 */
public class MessageQConsumer extends AbstractSimpleRMQ {

  private static final String QUEUE_NAME = "simple-queue";

  public static void main(String... args) throws IOException, TimeoutException {

	initConnection();
	boolean durable = true;
	channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
	System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	Consumer consumer = new DefaultConsumer(channel) {
	  @Override
	  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
			  throws IOException {
		String message = new String(body, "UTF-8");
		System.out.println(String.format("Message {%s} read",message));
	  }

	};
	channel.basicConsume(QUEUE_NAME, true, consumer);
  }

}
