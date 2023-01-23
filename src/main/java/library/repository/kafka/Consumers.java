package library.repository.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;

import java.util.*;

public class Consumers {
    List<KafkaConsumer<UUID,String>> consumersGroup = new ArrayList<>();
    private static final String BOOTSTRAP_SERVERS_CONFIG = "kafka1:9192,kafka2:9292,kafka3:9392";
    private static final Integer NUMBER_OF_CONSUMERS = 5;

    public void initConsumers(){
        Properties consumerConfig =new Properties();
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, Topics.CONSUMER_GROUP_NAME);
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
        for (int i = 0; i < NUMBER_OF_CONSUMERS; i++) {
            KafkaConsumer<UUID,String> kafkaConsumer = new KafkaConsumer(consumerConfig);
            kafkaConsumer.subscribe(Arrays.asList(Topics.RENT_TOPIC.getTopic()));
            consumersGroup.add(kafkaConsumer);
        }
    }

}
