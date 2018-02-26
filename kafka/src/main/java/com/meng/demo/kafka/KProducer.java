package com.meng.demo.kafka;


import org.apache.kafka.clients.producer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class KProducer {

    private static final Logger logger = LogManager.getLogger(KProducer.class);

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaUtil.bootstrapServers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("test-topic-meng", Integer.toString(i), Integer.toString(i));
            producer.send(record,
                    new Callback() {
                        public void onCompletion(RecordMetadata metadata, Exception e) {
                            if (e != null) {
                                logger.error(e.getMessage(), e);
                            } else {
                                logger.info("The offset of the record we just sent is: " + metadata.offset());
                            }
                        }
                    });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        producer.close();
    }


}
