package com.xcompany.xproject.kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConsumer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TestConsumer.class);
    
    public static void main(String[] args) {
        Properties properties = new Properties();
        // bin/kafka-topics.sh
        properties.put("zookeeper.connect", "node-01:2181,node-02:2181,node-03:2181");
        // kafka-console-producer.sh
        properties.put("metadata.broker.list", "node-02:9092,node-03:9092,node-04:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // kafka-console-consumer.sh
        properties.put("bootstrap.servers", "node-02:9092,node-03:9092,node-04:9092");
        
        // must sepc group.id
        properties.put("group.id", "test-group-new");
        properties.put("auto.offset.reset", "earliest");
        
        Consumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList("order-r"));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000);   // ms
                for (ConsumerRecord<String, String> record : records) {
                    LOGGER.info("offset = {}, key = {}, value = {}\n", record.offset(), record.key(), record.value());
                }
            }
        } catch (Exception e) {
        } finally {
            consumer.close();
        }
        
    }
    
}
