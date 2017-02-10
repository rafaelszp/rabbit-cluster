package szp.rafael.rabbitcluster.simplerabbitmqclient.queue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by rafael on 2/10/17.
 */
public class MessageQConsumer {

  public static final String RABBITMQ_SERVER_HOST = "localhost";
  private static final String QUEUE_NAME = "simple-queue";
  private static final String USERNAME = "user";
  private static final String PASSWORD = "password";
  private static ConnectionFactory factory;
  private static Connection connection;
  private static Channel channel;

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


  private static void initConnection() throws IOException, TimeoutException {
	factory = new ConnectionFactory();
	factory.setHost(RABBITMQ_SERVER_HOST);
	factory.setUsername(USERNAME);
	factory.setPassword(PASSWORD);
	connection = factory.newConnection();
	channel = connection.createChannel();
  }

  private static void endConnection() throws IOException, TimeoutException {
	channel.close();
	connection.close();
  }


}
