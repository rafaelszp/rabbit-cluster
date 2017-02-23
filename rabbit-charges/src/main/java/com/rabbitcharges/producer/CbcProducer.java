package com.rabbitcharges.producer;

import com.rabbitcharges.util.AbstractRabbit;
import com.rabbitcharges.util.RabbitFactory;
import com.rabbitcharges.vo.Charge;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.tools.json.JSONReader;
import com.rabbitmq.tools.json.JSONWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.rabbitmq.client.MessageProperties.PERSISTENT_TEXT_PLAIN;

/**
 * Created by rafael on 2/22/17.
 */
public class CbcProducer extends AbstractRabbit {

  private static String companies[] = {"goias", "tocantins"};

  public static void main(String... args) throws Exception {

	CbcProducer cbcProducer = new CbcProducer();
	cbcProducer.getChannel().confirmSelect();
	RabbitFactory.setupQX(cbcProducer.getChannel());

	Files.write(Paths.get("cbc-error.log"), "".getBytes("UTF-8"), StandardOpenOption.TRUNCATE_EXISTING);
	int messageCount = 50;
	int confirmedCount = sendMessages(cbcProducer, messageCount);
	while (messageCount > confirmedCount) {
	  int count = sendRemainingMessages(cbcProducer);
	  confirmedCount += count;
	}
	cbcProducer.getChannel().close();
	cbcProducer.getConnection().close();
	System.exit(0);
  }

  private static void send(CbcProducer cbcProducer, String routingKey, String message) throws IOException {
	cbcProducer.getChannel()
			.basicPublish(RabbitFactory.X_CBC, routingKey,
					true, false,
					PERSISTENT_TEXT_PLAIN,
					message.getBytes("UTF-8"));
  }

  private static int sendMessages(CbcProducer cbcProducer, final long messageCount) throws IOException, InterruptedException {
	JSONWriter jsonWriter = new JSONWriter();
	long errorCount = 0;
	AtomicInteger confirmedCount = new AtomicInteger(0);
	cbcProducer.getChannel().addConfirmListener(new ConfirmListener() {
	  @Override
	  public void handleAck(long l, boolean b) throws IOException {
		System.out.printf("Acked %s\n", l);
		confirmedCount.incrementAndGet();
	  }

	  @Override
	  public void handleNack(long l, boolean b) throws IOException {

	  }
	});
	for (int i = 0; i < messageCount; i++) {
	  String company = companies[new Random().nextInt(2)];
	  Long contId = new Random().nextLong();
	  double v = new Random().nextDouble() * contId;
	  BigDecimal value = BigDecimal.valueOf(v);
	  Charge charge = new Charge(UUID.randomUUID(), company, contId, value);
	  String message = jsonWriter.write(charge);
	  String routingKey = "company.cbc." + company;
	  try {
		System.out.printf("Sending %s -> %s\n", i, message);
		send(cbcProducer, routingKey, message);
	  } catch (Exception e) {
		System.err.printf("%s\n", e.getMessage());
		cbcProducer.appendToLog(message);
		errorCount++;
	  }
	  Thread.sleep(1_000L);
	}
	cbcProducer.getChannel().waitForConfirms();
	System.err.printf("Error count {%s} - Confirmed count {%s}\n", errorCount, confirmedCount.get());
	return confirmedCount.get();
  }

  private static int sendRemainingMessages(CbcProducer producer) {
	AtomicInteger sentCount = new AtomicInteger(0);
	try (Stream<String> lines = Files.lines(Paths.get("cbc-error.log"))) {
	  lines
			  .filter(l -> l != null && l.length() > 0)
			  .forEach(line -> {
						JSONReader jsonReader = new JSONReader();
						Map<String, Object> charge = (Map<String, Object>) jsonReader.read(line);
						Object company = charge.get("company");
						String routingKey = "company.cbc." + company;
						try {
						  send(producer, routingKey, line);
						  sentCount.incrementAndGet();
						} catch (IOException e) {
						  System.err.printf("%s\n", e);
						}
					  }
			  );
	} catch (IOException e) {
	  System.err.printf("%s\n", e.getMessage());
	}
	return sentCount.get();
  }

  protected void appendToLog(String message) throws IOException {
	Path path = Paths.get("cbc-error.log");
	System.out.printf("Recording message to file %s\n", path.getFileName());
	try {
	  Files.write(path, message.getBytes("UTF-8"), StandardOpenOption.CREATE_NEW);
	} catch (IOException e) {
	}
	String log = message + "\n";
	Files.write(path, log.getBytes("UTF-8"), StandardOpenOption.APPEND);

  }

}
