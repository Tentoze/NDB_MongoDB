package library.repository.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public void consumeTopicsByGroup() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (KafkaConsumer<UUID,String> consumer : consumersGroup) {
            executorService.execute(() -> consume(consumer));
        }
        Thread.sleep(10000);
        for (KafkaConsumer<UUID,String> consumer : consumersGroup) {
            consumer.wakeup();
        }
        executorService.shutdown();
    }

    private void consume(KafkaConsumer<UUID,String> consumer) {
        try {
            consumer.poll(0);
            Set<TopicPartition> consumerAssignment = consumer.assignment();
            System.out.println(consumer.groupMetadata().memberId() + " " + consumerAssignment);
            consumer.seekToBeginning(consumerAssignment);
            Duration timeout = Duration.of(100, ChronoUnit.MILLIS);
            MessageFormat formatter = new MessageFormat("Konsument {5}, Temat{0}, partycja {1}, offset {2, number, integer}, klucz {3}, wartość {4}");
            while (true) {
                ConsumerRecords<UUID,String> records = consumer.poll(timeout);
                for(ConsumerRecord<UUID,String> record : records) {
                    String result = formatter.format(new Object[]{
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value(),
                            consumer.groupMetadata().memberId()
                    });
                    System.out.println(result);
                }
            }
        } catch (WakeupException we) {
            System.out.println("Job ended");
        }
    }
}
