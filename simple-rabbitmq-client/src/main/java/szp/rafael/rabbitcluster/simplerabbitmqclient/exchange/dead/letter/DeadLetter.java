package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.dead.letter;

import com.rabbitmq.client.MessageProperties;
import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by igor on 2/20/17.
 */
public class DeadLetter extends AbstractSimpleRMQ {

    public static String EXCHANGE_NAME = "exchange.dead.letter";

    public static String QUEUE_NAME = "queue.dead.letter";

}
