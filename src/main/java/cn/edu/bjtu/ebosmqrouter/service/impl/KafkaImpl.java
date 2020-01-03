package cn.edu.bjtu.ebosmqrouter.service.impl;

import cn.edu.bjtu.ebosmqrouter.service.Mq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service("kafka")
public class KafkaImpl implements Mq {
    private KafkaTemplate kafkaTemplate;

    @Autowired
    public KafkaImpl(KafkaTemplate kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String destinationName, String message){
        kafkaTemplate.send(destinationName,message);
    }
}
