package com.rabbitcharges.producer;

import com.rabbitcharges.util.AbstractRabbit;
import com.rabbitcharges.util.RabbitFactory;
import com.rabbitcharges.vo.Charge;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.tools.json.JSONWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

import static com.rabbitmq.client.MessageProperties.*;

/**
 * Created by rafael on 2/22/17.
 */
public class CbcProducer extends AbstractRabbit{

	private static String companies[] = {"goias","tocantins"};

	public static void main(String... args) throws Exception {

	  CbcProducer cbcProducer = new CbcProducer();
	  RabbitFactory.setupQX(cbcProducer.getChannel());
	  JSONWriter jsonWriter = new JSONWriter();

	  for(;;){
	    String company = companies[new Random().nextInt(2)];
	    Long contId = new Random().nextLong();
		double v = new Random().nextDouble() * contId;
		BigDecimal value = BigDecimal.valueOf(v);
	    Charge charge = new Charge(UUID.randomUUID(),company,contId,value);
	    String message = jsonWriter.write(charge);
	    String routingKey = "company.cbc."+company;
	    cbcProducer.getChannel()
				.basicPublish(RabbitFactory.X_CBC,routingKey, PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));

	    Thread.sleep(1000L);
	  }

	}

}
