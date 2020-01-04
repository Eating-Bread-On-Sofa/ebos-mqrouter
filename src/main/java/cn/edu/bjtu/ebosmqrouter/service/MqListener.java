package cn.edu.bjtu.ebosmqrouter.service;

import cn.edu.bjtu.ebosmqrouter.service.impl.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Order(1)
public class MqListener implements ApplicationRunner {
    @Autowired
    MqFactory mqFactory;
    @Value("${mq}")
    private String name;

    @Override
    public void run(ApplicationArguments arguments){
        new Thread(() -> {
            KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(KafkaConfig.getProperties());
            kafkaConsumer.subscribe(Arrays.asList("test"));
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("-----------------");
                    System.out.printf("offset = %d, value = %s", record.offset(), record.value());
                    System.out.println();
                }
            }
        }).start();
    }
}
