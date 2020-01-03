package cn.edu.bjtu.ebosmqrouter.service.impl;

import cn.edu.bjtu.ebosmqrouter.service.Mq;
import cn.edu.bjtu.ebosmqrouter.service.MqFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.kafka.core.KafkaTemplate;

public class MqFactoryImpl implements MqFactory {
    private KafkaTemplate kafkaTemplate;
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    public MqFactoryImpl(KafkaTemplate kafkaTemplate,JmsMessagingTemplate jmsMessagingTemplate){
        this.kafkaTemplate = kafkaTemplate;
        this.jmsMessagingTemplate = jmsMessagingTemplate;
    }

    @Override
    public Mq create(String name){
        switch (name){
            case "activemq" :
                return new ActiveMqImpl(jmsMessagingTemplate);
            case "kafka" :
                return new KafkaImpl(kafkaTemplate);
            default:
                return new ActiveMqImpl(jmsMessagingTemplate);
        }
    }
}
