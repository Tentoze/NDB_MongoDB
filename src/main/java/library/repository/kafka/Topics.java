package library.repository.kafka;

public enum Topics {
    RENT_TOPIC("rents"),
    CONSUMER_GROUP_NAME("consumergroup");

    private String topic;

    Topics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
