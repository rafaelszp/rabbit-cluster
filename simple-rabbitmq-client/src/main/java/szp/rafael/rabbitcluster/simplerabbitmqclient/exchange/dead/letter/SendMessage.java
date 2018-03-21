package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.dead.letter;

import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by igor on 2/20/17.
 */
public class SendMessage extends AbstractSimpleRMQ {

  public static String QUEUE_NAME = "queue.test";

  public static void main(String... args) throws IOException, TimeoutException {

    initConnection();

    QueueTest qt = new QueueTest(channel);

    qt.setExchangeDeadLetter(DeadLetter.EXCHANGE_NAME);

    qt.send("ok".getBytes());

    qt.send("dead.letter".getBytes());

    qt.send("requeue".getBytes());

    endConnection();

  }
}
