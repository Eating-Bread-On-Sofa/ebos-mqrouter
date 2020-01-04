package cn.edu.bjtu.ebosmqrouter.service.impl;

import cn.edu.bjtu.ebosmqrouter.service.Mq;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.Arrays;

@Service
public class KafkaImpl implements Mq {
    private KafkaTemplate kafkaTemplate;

    @Autowired
    public KafkaImpl(KafkaTemplate kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String topic, String message){
        kafkaTemplate.send(topic,message);
    }

    @Override
    public String getMessage(String topic){
//        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(KafkaConfig.getProperties());
//        kafkaConsumer.subscribe(Arrays.asList(topic));
//        ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
//        for (ConsumerRecord<String, String> record : records) {
//            System.out.println("-----------------");
//            System.out.printf("offset = %d, value = %s", record.offset(), record.value());
//            System.out.println();
//        }
//        return records.toString();
        return "";
    }
}
