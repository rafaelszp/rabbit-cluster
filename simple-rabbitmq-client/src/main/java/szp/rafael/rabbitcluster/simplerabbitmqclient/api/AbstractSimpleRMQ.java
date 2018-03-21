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

  public static final String PORT = System.getProperty("RABBIT_PORT", "5672");
  protected static final String RABBITMQ_SERVER_HOST = System.getProperty("RABBIT_HOST", "localhost");
  //  protected static final String RABBITMQ_SERVER_HOST = "10.21.6.136";
//
  protected static final String USERNAME = System.getProperty("RABBIT_USR", "user");
  protected static final String PASSWORD = System.getProperty("RABBIT_PWD", "password");
  protected static final String VHOST = System.getProperty("RABBIT_VHOST", "/");
  protected static ConnectionFactory factory;
  protected static Connection connection;
  protected static Channel channel;


  protected static void initConnection() throws IOException, TimeoutException {
    factory = new ConnectionFactory();
    factory.setHost(RABBITMQ_SERVER_HOST);
    factory.setUsername(USERNAME);
    factory.setPassword(PASSWORD);
    factory.setVirtualHost(VHOST);
    factory.setPort(Integer.valueOf(PORT));
    connection = factory.newConnection();
    channel = connection.createChannel();
  }

  public static void endConnection() throws IOException, TimeoutException {
    channel.close();
    connection.close();
  }

  public static long getPID() {
    String processName =
            java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
    return Long.parseLong(processName.split("@")[0]);
  }

}
