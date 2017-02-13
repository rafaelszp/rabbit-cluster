package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.topic;

import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.util.Random;

/**
 * Created by rafael on 2/13/17.
 */
public class AnimalTopicXProducer extends AbstractSimpleRMQ{

  private static final String EXCHANGE_NAME = "topic_logs";
  public static final int MESSAGE_COUNT = 1_000_000;

  public static void main(String... args) throws Exception{
	initConnection();

	channel.exchangeDeclare(
			EXCHANGE_NAME,
			"topic" //EXCHANGE TYPE - A direct exchange delivers messages to queues based on the message routing key.
	);

	long start = System.currentTimeMillis();
	for(int i=0;i< MESSAGE_COUNT;i++){
	  String message = String.format("LOG MESSAGE NUMBER {%s}",i);
	  boolean even = i % 2 ==0;
	  channel.basicPublish(
			  EXCHANGE_NAME,
			  routingKey(),
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

  private static String routingKey(){

    String key = "";
    String[] splitted = new String[4];
	String[] speeds = {"lazy.", "quick."};
	String[] colors = {"orange.","blue.","red.","purple."};
	String[] genders ={"","male.","female."};
	String[] species = {"rabbit","elephant","wolf","turtle"};

	String speed = speeds[new Random().nextInt(2)];
	String color = colors[new Random().nextInt(4)];
	String gender = genders[new Random().nextInt(3)];
	String specie = species[new Random().nextInt(4)];
	key = speed+color+gender+specie;

	return key;
  }

}
