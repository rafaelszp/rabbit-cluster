package szp.rafael.rabbitcluster.simplerabbitmqclient.queue.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * Created by rafael on 2/14/17.
 */
public class RPCClient extends AbstractSimpleRMQ{

  private static final String QUEUE_NAME = "rpc-queue";
  private String replyQueueName;

  public RPCClient(){
	try {
	  initConnection();
	  replyQueueName = channel.queueDeclare().getQueue();

	} catch (IOException e) {
	  e.printStackTrace();
	} catch (TimeoutException e) {
	  e.printStackTrace();
	}
  }

  public String call(String message) throws IOException, InterruptedException {

    String corrId = UUID.randomUUID().toString();
	AMQP.BasicProperties props = new AMQP.BasicProperties
			.Builder()
			.correlationId(corrId)
			.replyTo(replyQueueName)
			.build();
	channel.basicPublish("",QUEUE_NAME,props,message.getBytes("UTF-8"));

	final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);
	channel.basicConsume(replyQueueName,true,new DefaultConsumer(channel){
	  @Override
	  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	    if(properties.getCorrelationId().equals(corrId)){
			response.offer(new String(body,"UTF-8"));
		}
	  }
	});
	return response.take();
  }



}
