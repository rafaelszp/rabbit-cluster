package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.direct;

import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

/**
 * Created by rafael on 2/13/17.
 */
public class LogEvenOddXMQProducer extends AbstractSimpleRMQ{

  private static final String EXCHANGE_NAME = "direct_logs";
  public static final int MESSAGE_COUNT = 1000000;

  public static void main(String... args) throws Exception{
	initConnection();

	channel.exchangeDeclare(
			EXCHANGE_NAME,
			"direct" //EXCHANGE TYPE - A direct exchange delivers messages to queues based on the message routing key.
	);

	long start = System.currentTimeMillis();
	for(int i=0;i< MESSAGE_COUNT;i++){
	  String message = String.format("LOG MESSAGE NUMBER {%s}",i);
	  boolean even = i % 2 ==0;
	  channel.basicPublish(
			  EXCHANGE_NAME,
			  even ? "even":"odd",//Routing KEY
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
