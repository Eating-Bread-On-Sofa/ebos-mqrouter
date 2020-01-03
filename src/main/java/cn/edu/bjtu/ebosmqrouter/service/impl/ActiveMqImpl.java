package cn.edu.bjtu.ebosmqrouter.service.impl;

import cn.edu.bjtu.ebosmqrouter.service.Mq;
import com.alibaba.fastjson.JSONObject;
import org.apache.activemq.command.ActiveMQTopic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsMessagingTemplate;
import javax.jms.Destination;

@Service("activemq")
public class ActiveMqImpl implements Mq {
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    public ActiveMqImpl(JmsMessagingTemplate jmsMessagingTemplate){
        this.jmsMessagingTemplate = jmsMessagingTemplate;
    }
//    @Bean
//    JmsListenerContainerFactory<?> topicContainerFactory(ConnectionFactory connectionFactory){
//        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setPubSubDomain(true);
//        return factory;
//    }

    public void send(String destinationName, JSONObject message) {
        System.out.println("发送"+destinationName+"消息： " + message);
        Destination destination = new ActiveMQQueue(destinationName);
        jmsMessagingTemplate.convertAndSend(destination, message);
    }

    @Override
    public void publish(String destinationName, String message) {
        Destination destination = new ActiveMQTopic(destinationName);
        System.out.println("发布"+destinationName+"消息 " + message);
        jmsMessagingTemplate.convertAndSend(destination, message);
    }

//    @JmsListener(destination = "test.topic", containerFactory = "topicContainerFactory")
//    public void subscribeTest(JSONObject msg) {
//        System.out.println("收到订阅的消息:" + msg);
//    }
//
//    @JmsListener(destination = "test.queue")
//    public void receiveTest(String msg) {
//        System.out.println("收到消息： " + msg);
//    }
}
