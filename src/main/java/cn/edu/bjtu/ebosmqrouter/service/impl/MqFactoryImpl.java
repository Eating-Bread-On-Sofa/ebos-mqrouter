package cn.edu.bjtu.ebosmqrouter.service.impl;

import cn.edu.bjtu.ebosmqrouter.service.Mq;
import cn.edu.bjtu.ebosmqrouter.service.MqFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MqFactoryImpl implements MqFactory {
    @Autowired
    KafkaTemplate kafkaTemplate;
    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;
    @Value("${mq}")
    private String name;

    @Override
    public Mq create(){
        switch (this.name){
            case "activemq" :
                return new ActiveMqImpl(jmsMessagingTemplate);
            case "kafka" :
                return new KafkaImpl(kafkaTemplate);
            default:
                return new ActiveMqImpl(jmsMessagingTemplate);
        }
    }
}
