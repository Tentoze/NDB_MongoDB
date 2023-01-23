package library.repository.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import library.model.Rent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.internals.Topic;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDSerializer;

import javax.json.Json;
import javax.json.bind.Jsonb;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer {
    private static final String ID_CONFIG = "local";
    private static final String BOOTSTRAP_SERVERS_CONFIG = "kafka1:9192,kafka2:9292,kafka3:9392";
    protected org.apache.kafka.clients.producer.Producer<UUID, String> producer;

    public Producer() throws ExecutionException, InterruptedException {
        initProducer();
    }

    public void initProducer() throws ExecutionException, InterruptedException {
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, ID_CONFIG);
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
        producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
        this.producer = new KafkaProducer<>(producerConfig);
    }

    public void send(Rent rent) throws JsonProcessingException, ExecutionException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonObject = mapper.writeValueAsString(rent);
        ProducerRecord<UUID, String> record = new ProducerRecord<>(Topics.RENT_TOPIC.getTopic(),
                rent.getEntityId().getUUID(), jsonObject);
        Future<RecordMetadata> sent = producer.send(record);
        RecordMetadata recordMetadata = sent.get();
    }
}
