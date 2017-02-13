package szp.rafael.rabbitcluster.simplerabbitmqclient.queue;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by rafael on 2/10/17.
 */
public class MessageQueueProducer extends AbstractSimpleRMQ{

  protected static final String QUEUE_NAME = "simple-queue";

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
	channel.basicPublish(
			"", //exchange name - blank means default exchange
			QUEUE_NAME, //queue name
			MessageProperties.PERSISTENT_TEXT_PLAIN, //telling RabbitMQ to write messages to disk
			message.getBytes()//message
	);
  }

}
