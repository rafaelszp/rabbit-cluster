package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.dead.letter;

import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by igor on 2/20/17.
 */
public class ConsumeMessage extends AbstractSimpleRMQ{

    public static void main(String... args) throws Exception {

        initConnection();

        QueueTest qt = new QueueTest(channel);

        qt.consumeWithConfirm();

        endConnection();

    }
}
