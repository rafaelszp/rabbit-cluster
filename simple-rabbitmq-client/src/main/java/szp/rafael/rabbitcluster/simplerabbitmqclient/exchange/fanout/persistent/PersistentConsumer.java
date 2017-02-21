package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.fanout.persistent;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rafael on 2/20/17.
 */
public class PersistentConsumer extends AbstractSimpleRMQ{


  public static void main(String... args) throws Exception{

	initConnection();

	channel.exchangeDeclare(
			PersistentBroadcaster.EXCHANGE_NAME,
			PersistentBroadcaster.EXCHANGE_TYPE,
			true
	);


	channel.queueBind(PersistentBroadcaster.QUEUE_NAME,
			PersistentBroadcaster.EXCHANGE_NAME,""
	);

	System.out.println("Receiving messages ... ");
	Consumer consumer = new DefaultConsumer(channel){
	  @Override
	  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		String message = new String(body,"UTF-8").substring(1,100);
		System.out.printf("\nReceived message {%s}",message);
	  }
	};

	channel.basicConsume(PersistentBroadcaster.QUEUE_NAME,true,consumer);

  }
}
