package szp.rafael.rabbitcluster.simplerabbitmqclient.queue.rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * Created by rafael on 2/14/17.
 */
public class RPCCaller {

  private static int MESSAGE_COUNT = 50_000;

  public static void main(String... args) throws IOException, InterruptedException, TimeoutException {
    long start = System.currentTimeMillis();
    List<String> response = new ArrayList<>();
    for (int i = 0; i < MESSAGE_COUNT; i++) {
      RPCClient rpcClient = new RPCClient();
      Integer number = new Random().nextInt(10);
      response.add(rpcClient.call(number.toString()));
      rpcClient.endConnection();
    }
    long end = System.currentTimeMillis();
    double evaluated = (end - start) * 0.001;
    double tpt = MESSAGE_COUNT / evaluated;
    response.forEach(s -> {
      System.out.printf("Response {%s}\n", s);
    });
    System.out.printf("Message count: {%s} | Evaluated {%.3f}s | Throughput {%.2f}m/s\n", MESSAGE_COUNT, evaluated, tpt);


  }
}
