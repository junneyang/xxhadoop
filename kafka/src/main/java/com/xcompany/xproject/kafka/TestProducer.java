package com.xcompany.xproject.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestProducer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TestProducer.class);
    
    public static void main(String[] args) {
        Properties properties = new Properties();
        // bin/kafka-topics.sh
        properties.put("zookeeper.connect", "node-01:2181,node-02:2181,node-03:2181");
        // kafka-console-producer.sh
        properties.put("metadata.broker.list", "node-02:9092,node-03:9092,node-04:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // kafka-console-consumer.sh
        properties.put("bootstrap.servers", "node-02:9092,node-03:9092,node-04:9092");
        
        
        Producer<String, String> producer = new KafkaProducer<String, String>(properties);
        
        LOGGER.info("roduce start...");
        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> msg = new ProducerRecord<String, String>("order-r", "name", "Hello_XXX_" + i);
            producer.send(msg);
        }
        producer.close();
        LOGGER.info("produce end...");
    }
}
