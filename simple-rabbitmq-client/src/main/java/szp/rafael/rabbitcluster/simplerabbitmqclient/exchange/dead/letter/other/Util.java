package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.dead.letter.other;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by rafael on 2/21/17.
 */
public class Util {

  public static final String DEAD_LETTER_EXCHANGE_NAME = "other.exchange.dead.letter";
  public static final String QUEUE_NAME = "other.queue.sample";
  protected static final String RABBITMQ_SERVER_HOST = "localhost";
  protected static final String USERNAME = "user";
  protected static final String PASSWORD = "password";
  protected static final String EXCHANGE_NAME = "other.exchange.sample";
  private static final String EXCHANGE_TYPE = "direct";
  private static final String DEAD_LETTER_EXCHANGE_TYPE = "direct";
  private static final String DEAD_LETTER_MAIL = "dead.letter.mail";

  public static ConnectionFactory createFactory() {

    ConnectionFactory factory;
    factory = new ConnectionFactory();
    factory.setHost(RABBITMQ_SERVER_HOST);
    factory.setUsername(USERNAME);
    factory.setPassword(PASSWORD);
    return factory;
  }

  public static Channel createChannel(Connection connection) throws IOException {
    return connection.createChannel();
  }

  public static void setupMessageAssets(Channel channel) throws IOException {
    channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true);
    channel.exchangeDeclare(DEAD_LETTER_EXCHANGE_NAME, DEAD_LETTER_EXCHANGE_TYPE, true);

    Map<String, Object> arguments = new HashMap<String, Object>();
    arguments.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_NAME);
    arguments.put("x-message-ttl", 10000);

    channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

    channel.queueDeclare(DEAD_LETTER_MAIL, true, false, false, null);
    channel.queueBind(DEAD_LETTER_MAIL, DEAD_LETTER_EXCHANGE_NAME, "");
  }

}
