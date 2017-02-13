package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.fanout;

import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.util.Random;

/**
 * Created by rafael on 2/13/17.
 */
public class LogXMQProducer extends AbstractSimpleRMQ {

  private static final String EXCHANGE_NAME = "logs";
  public static final int MESSAGE_COUNT = 1000000;

  public static void main(String... args) throws Exception{
    initConnection();

    channel.exchangeDeclare(
    		EXCHANGE_NAME,
			"fanout" //EXCHANGE TYPE - A fanout exchange routes messages to all of the queues that are bound
			// to it and the routing key is ignored. Ideal for broadcast
	);

	long start = System.currentTimeMillis();
	for(int i=0;i< MESSAGE_COUNT;i++){
      String message = String.format("LOG MESSAGE NUMBER {%s}",i);
      channel.basicPublish(
      		EXCHANGE_NAME,
			"",//QUEUE NAME
			null,
			message.getBytes()
	  );
	}
	long end = System.currentTimeMillis();
	double evaluated = (end-start)*0.001;
	double thpt = MESSAGE_COUNT/evaluated;
	System.out.println(String.format("{%s} messages send in {%.3f} seconds. Throughput: {%.3f}",MESSAGE_COUNT,evaluated,thpt));
	endConnection();
  }

}
