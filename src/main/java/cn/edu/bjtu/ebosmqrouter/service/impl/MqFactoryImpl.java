package cn.edu.bjtu.ebosmqrouter.service.impl;

import cn.edu.bjtu.ebosmqrouter.service.MqConsumer;
import cn.edu.bjtu.ebosmqrouter.service.MqProducer;
import cn.edu.bjtu.ebosmqrouter.service.MqFactory;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MqFactoryImpl implements MqFactory {
    @Autowired
    KafkaTemplate kafkaTemplate;
    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;
    @Value("${mq}")
    private String name;

    @Override
    public MqProducer createProducer(){
        switch (this.name){
            case "activemq" :
                return new ActiveMqProducerImpl(jmsMessagingTemplate);
            case "kafka" :
                return new KafkaProducerImpl(kafkaTemplate);
            default:
                return new ActiveMqProducerImpl(jmsMessagingTemplate);
        }
    }

    @Override
    public MqConsumer createConsumer(String topic){
        switch (this.name){
            case "activemq" :
                return new ActiveMqConsumerImpl(topic);
            case "kafka" :
                return new KafkaConsumerImpl(topic);
            default:
                return new KafkaConsumerImpl(topic);
        }
    }
}
