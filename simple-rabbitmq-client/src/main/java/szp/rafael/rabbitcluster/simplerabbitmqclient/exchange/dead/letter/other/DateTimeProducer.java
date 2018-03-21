package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.dead.letter.other;

import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

/**
 * Created by rafael on 2/21/17.
 */
public class DateTimeProducer extends AbstractOtherSample {


  public static void main(String... args) throws IOException, TimeoutException, InterruptedException {

    DateTimeProducer dateTimeProducer = new DateTimeProducer();


    for (; ; ) {
      LocalDateTime now = LocalDateTime.now();
      String message = now.toString();
      if (System.currentTimeMillis() % 3 == 0) {
        message = "requeue";
      }
      dateTimeProducer.getChannel()
              .basicPublish(Util.EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
      Thread.sleep(1000L);
    }

  }

}
