package szp.rafael.rabbitcluster.simplerabbitmqclient.api;

/**
 * Created by igor on 2/20/17.
 */
public enum AMQArguments {

  X_DEAD_LETTER_EXCHANGE("x-dead-letter-exchange"),
  X_MAX_LENGTH("x-max-length"),
  X_MESSAGE_TTL("x-message-ttl");


  private String property;

  private AMQArguments(String property) {
    this.property = property;
  }

  public String getProperty() {
    return property;
  }
}
