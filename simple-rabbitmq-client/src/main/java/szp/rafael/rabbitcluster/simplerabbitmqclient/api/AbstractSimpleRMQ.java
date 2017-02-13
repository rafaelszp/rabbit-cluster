package szp.rafael.rabbitcluster.simplerabbitmqclient.api;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by rafael on 2/13/17.
 */
public abstract class AbstractSimpleRMQ {

  protected static final String RABBITMQ_SERVER_HOST = "localhost";

  protected static final String USERNAME = "user";
  protected static final String PASSWORD = "password";
  protected static ConnectionFactory factory;
  protected static Connection connection;
  protected static Channel channel;


  protected static void initConnection() throws IOException, TimeoutException {
	factory = new ConnectionFactory();
	factory.setHost(RABBITMQ_SERVER_HOST);
	factory.setUsername(USERNAME);
	factory.setPassword(PASSWORD);
	connection = factory.newConnection();
	channel = connection.createChannel();
  }

  protected static void endConnection() throws IOException, TimeoutException {
	channel.close();
	connection.close();
  }
}
