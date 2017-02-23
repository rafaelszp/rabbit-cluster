package com.rabbitcharges.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by rafael on 2/22/17.
 */
public class AbstractRabbit {

  private Channel channel;
  private Connection connection;

  public AbstractRabbit(){
	try {
	  this.connection = RabbitFactory.newConnectionFactory().newConnection();
	  this.channel = RabbitFactory.newChannel(connection);
	} catch (IOException e) {
	  e.printStackTrace();
	} catch (TimeoutException e) {
	  e.printStackTrace();
	}
  }


  public Channel getChannel() {
	return channel;
  }

  public Connection getConnection() {
	return connection;
  }

  public void close(){
	try {
	  connection.close();
	  channel.close();
	} catch (IOException e) {
	  e.printStackTrace();
	} catch (TimeoutException e) {
	  e.printStackTrace();
	}
  }
}
