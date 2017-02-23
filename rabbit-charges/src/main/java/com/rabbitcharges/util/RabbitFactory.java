package com.rabbitcharges.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by rafael on 2/22/17.
 */
public class RabbitFactory {

  protected static final String RABBITMQ_SERVER_HOST = "localhost";
  protected static final String USERNAME = "user";
  protected static final String PASSWORD = "password";

  public static final String X_CBC="x_cbc";
  public static final String Q_CBC_GOIAS="q_cbc_goias";
  public static final String Q_CBC_TOCANTINS="q_cbc_tocantins";
  public static final String Q_CBC_PROCESSED="q_cbc_processed";
  private static final String EXCHANGE_TYPE = "topic";
  private static String Q_CBC_TMP="q_cbc_tmp";


  protected RabbitFactory(){}

  public static ConnectionFactory newConnectionFactory(){
	ConnectionFactory factory;
	factory = new ConnectionFactory();
	factory.setHost(RABBITMQ_SERVER_HOST);
	factory.setUsername(USERNAME);
	factory.setPassword(PASSWORD);
	return factory;
  }

  public static Channel newChannel(Connection connection) throws IOException {
	return connection.createChannel();
  }

  public static void setupQX(Channel channel) throws IOException {

	HashMap<String, Object> qcbcArguments = new HashMap<>();
	HashMap<String, Object> qcbcTmpArguments = new HashMap<>();
	qcbcTmpArguments.put("x-message-ttl", 86_400_000);

	channel.exchangeDeclare(X_CBC,EXCHANGE_TYPE,true);

	channel.queueDeclare(Q_CBC_GOIAS,true,false,false,qcbcArguments);
	channel.queueDeclare(Q_CBC_TOCANTINS,true,false,false,qcbcArguments);
	channel.queueDeclare(Q_CBC_TMP,true,false,false,qcbcTmpArguments);
	channel.queueDeclare(Q_CBC_PROCESSED,true,false,false,null);

	channel.queueBind(Q_CBC_GOIAS,X_CBC,"company.cbc.goias");
	channel.queueBind(Q_CBC_TOCANTINS,X_CBC,"company.cbc.tocantins");
	channel.queueBind(Q_CBC_TMP,X_CBC,"company.cbc.*");

  }

}
