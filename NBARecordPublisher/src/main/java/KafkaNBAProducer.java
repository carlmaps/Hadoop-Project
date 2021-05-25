import com.fasterxml.jackson.databind.JsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.Properties;
import java.util.function.Consumer;

public class KafkaNBAProducer implements Consumer<NBARecord> {

    private final String topic;
    private final KafkaProducer<byte[], byte[]> producer;
    private JsonRecordSerializer<NBARecord> recordSerializer;

    public KafkaNBAProducer(String topic, String broker){
        this.topic = topic;
        this.producer = new KafkaProducer<>(createKafkaProperties(broker));
        this.recordSerializer = new JsonRecordSerializer<>();

    }
    @Override
    public void accept(NBARecord nbaRecord) {

        byte[] data = recordSerializer.toJSONBytes(nbaRecord);
        ProducerRecord<byte[], byte[]> kafkaRecord = new ProducerRecord<>(topic, data);
        producer.send(kafkaRecord);

        // Control the speed of messages through sleep
        try {
            Thread.sleep(500);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

    }

    private static Properties createKafkaProperties(String brokers) {
        Properties kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getCanonicalName());
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getCanonicalName());
        return kafkaProps;
    }
}
