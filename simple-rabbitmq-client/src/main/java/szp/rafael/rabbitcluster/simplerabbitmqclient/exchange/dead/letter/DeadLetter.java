package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.dead.letter;

import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

/**
 * Created by igor on 2/20/17.
 */
public class DeadLetter extends AbstractSimpleRMQ {

  public static String EXCHANGE_NAME = "exchange.dead.letter";

  public static String QUEUE_NAME = "queue.dead.letter";

}
