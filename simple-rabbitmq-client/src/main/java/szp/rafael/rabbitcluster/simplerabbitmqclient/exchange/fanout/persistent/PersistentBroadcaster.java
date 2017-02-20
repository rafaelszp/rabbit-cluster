package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.fanout.persistent;

import com.rabbitmq.client.MessageProperties;
import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.util.Arrays;

/**
 * Created by rafael on 2/20/17.
 */
public class PersistentBroadcaster extends AbstractSimpleRMQ {

  public static final String EXCHANGE_TYPE = "fanout";
  public static final String EXCHANGE_NAME = "persistent."+EXCHANGE_TYPE+".test";
  public static final String QUEUE_NAME ="persistent.queue.test";
  public static final int MESSAGE_COUNT = 10;

  public static void main(String... args) throws Exception{
	initConnection();

	boolean durableExchange = true;
	channel.exchangeDeclare(
			EXCHANGE_NAME,
			EXCHANGE_TYPE,
			durableExchange
	);

	channel.queueDeclare(QUEUE_NAME,true,false,false,null);
	channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");

	long start = System.currentTimeMillis();
	for(int i=0;i< MESSAGE_COUNT;i++){
	  char[] chars = new char[1024*1024*100];
	  Arrays.fill(chars,'f');
	  String message = new String(chars);
	  channel.basicPublish(
			  EXCHANGE_NAME,
			  "",
			  MessageProperties.PERSISTENT_TEXT_PLAIN,
			  message.getBytes()
	  );
	}
	long end = System.currentTimeMillis();
	double evaluated = (end-start)*0.001;
	double thpt = MESSAGE_COUNT/evaluated;
	System.out.println(String.format("{%s} messages send in {%.3f} seconds. Throughput: {%.2f}msg/s",MESSAGE_COUNT,evaluated,thpt));
	endConnection();
  }

}
