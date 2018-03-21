package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.dead.letter.other;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by rafael on 2/21/17.
 */
public class AbstractOtherSample {

  protected Channel channel;

  public AbstractOtherSample() {
    try {
      init();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
    }
  }

  protected void init() throws IOException, TimeoutException {
    channel = Util.createChannel(Util.createFactory().newConnection());
    Util.setupMessageAssets(channel);
  }

  public Channel getChannel() {
    return channel;
  }


  public DefaultConsumer consumer(Channel channel) {
    return new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        if (envelope.getDeliveryTag() % 2 == 0) {
          getChannel().basicNack(envelope.getDeliveryTag(), false, false);
          System.err.println("N'ackd: consumerTag = [" + consumerTag + "], envelope = [" + envelope + "], properties = [" + properties + "], body = [" + message + "]");
        } else if (message.equals("requeue")) {
          getChannel().basicNack(envelope.getDeliveryTag(), false, true);
          System.err.println("Requeued: consumerTag = [" + consumerTag + "], envelope = [" + envelope + "], properties = [" + properties + "], body = [" + message + "]");
        } else {
          getChannel().basicAck(envelope.getDeliveryTag(), false);
          System.out.println("Ackd: consumerTag = [" + consumerTag + "], envelope = [" + envelope + "], properties = [" + properties + "], body = [" + message + "]");
        }
      }
    };
  }
}
