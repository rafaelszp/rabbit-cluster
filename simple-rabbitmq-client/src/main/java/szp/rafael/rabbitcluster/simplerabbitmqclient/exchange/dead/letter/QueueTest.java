package szp.rafael.rabbitcluster.simplerabbitmqclient.exchange.dead.letter;

import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties;
import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AMQArguments;
import szp.rafael.rabbitcluster.simplerabbitmqclient.api.AbstractSimpleRMQ;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by igor on 2/20/17.
 */
public class QueueTest extends AbstractSimpleRMQ{

    private final String QUEUE_NAME = "queue.test";
    private Channel channel;
    private Map<String, Object> map;
    private BasicProperties basicProperties = MessageProperties.PERSISTENT_TEXT_PLAIN;
    private Boolean reject;
    private Long deliveryTag;

    public QueueTest(Channel channel){
        this.channel = channel;
        this.map = new HashMap<>();

    }

    public void send(byte[] b) throws IOException {

        boolean autoAck = false;
        channel.queueDeclare(QUEUE_NAME, autoAck, false, false, map);

        channel.basicPublish(
                "",
                QUEUE_NAME,
                this.basicProperties,
                b);
    }

    public void setExchangeDeadLetter(String exchangeName){

        map.put(AMQArguments.X_DEAD_LETTER_EXCHANGE.getProperty(),exchangeName);

    }

    public void consume() throws Exception{

        System.out.println("Receiving messages ... ");

        channel.basicConsume(this.QUEUE_NAME,true,getConsumer());

    }

    public void consumeWithConfirm() throws Exception{

        System.out.println("Receiving messages with confirmation ... ");

        boolean autoAck = false;

        GetResponse response = channel.basicGet(this.QUEUE_NAME, autoAck);

        if(response == null){

        } else {

            long deliveryTag = response.getEnvelope().getDeliveryTag();

            String body = new String(response.getBody());

            if(body.equals("requeue")){

                channel.basicNack(deliveryTag, false, true);

            } else if(body.equals("dead.letter")) {

                channel.basicNack(deliveryTag, false, false);

            } else if(body.equals("ok")){

                channel.basicAck(deliveryTag, false);

            }

            System.out.println(body);

        }


    }

    public Consumer getConsumer(){

        return new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                String message = new String(body,"UTF-8");

                System.out.printf("\nReceived message {%s}",message);

            }

        };

    }


}
