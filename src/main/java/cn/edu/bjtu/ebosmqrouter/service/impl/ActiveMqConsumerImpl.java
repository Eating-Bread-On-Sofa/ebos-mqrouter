package cn.edu.bjtu.ebosmqrouter.service.impl;

import cn.edu.bjtu.ebosmqrouter.service.MqConsumer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.util.Map;

public class ActiveMqConsumerImpl implements MqConsumer {
    private MessageConsumer messageConsumer;
    public static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

    public ActiveMqConsumerImpl(String topic){
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topic);
            MessageConsumer consumer = session.createConsumer(destination);
            this.messageConsumer = consumer;
        }catch (Exception e){}
    }

    @Override
    public String subscribe(){
        try {
            ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) messageConsumer.receive();
            return activeMQTextMessage.getText();
        }catch (Exception e){e.printStackTrace();return "啥也没收到";}
    }
}
