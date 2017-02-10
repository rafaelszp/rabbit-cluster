package szp.rafael.rabbitcluster.simplerabbitmqclient.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * Created by rafael on 2/10/17.
 */
public class MessageQueueProducer {

  public static final String RABBITMQ_SERVER_HOST = "localhost";
  private static final String QUEUE_NAME = "simple-queue";
  private static final String USERNAME = "user";
  private static final String PASSWORD = "password";
  private static ConnectionFactory factory;
  private static Connection connection;
  private static Channel channel;

  public static void main(String... args) throws IOException, TimeoutException {

	initConnection();

    Long start = System.currentTimeMillis();
    long count=10000L;
	for(long i = 0; i<count; i++) {
	  sendMessage(i);
	}
	Long end = System.currentTimeMillis();
	Double elapsed =  (end-start)/(1000D);
	Double throughput = count/elapsed;
	System.out.println(String.format("Sent {%s} messages in {%.3f} seconds. Throughput {%.2f} messages/s",count,elapsed,throughput));
	endConnection();
  }

  private static void sendMessage(long id) throws IOException {
	boolean durable = true; //allow recovery after restart or crash
	channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
	String message = "Hello World - "+id;
	channel.basicPublish("",QUEUE_NAME,
			MessageProperties.PERSISTENT_TEXT_PLAIN, //telling RabbitMQ to write messages to disk
			message.getBytes());
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
