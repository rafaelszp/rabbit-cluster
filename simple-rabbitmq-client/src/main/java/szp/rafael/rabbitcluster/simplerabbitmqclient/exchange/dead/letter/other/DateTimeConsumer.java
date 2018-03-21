package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.dead.letter.other;

/**
 * Created by rafael on 2/21/17.
 */
public class DateTimeConsumer extends AbstractOtherSample {

  public static void main(String... args) throws Exception {

    DateTimeConsumer dateTimeConsumer = new DateTimeConsumer();

    dateTimeConsumer.getChannel()
            .basicConsume(Util.QUEUE_NAME, false, dateTimeConsumer.consumer(dateTimeConsumer.getChannel()));

  }

}
