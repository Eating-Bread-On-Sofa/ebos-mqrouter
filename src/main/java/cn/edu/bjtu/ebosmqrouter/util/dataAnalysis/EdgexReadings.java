package cn.edu.bjtu.ebosmqrouter.util.dataAnalysis;

import cn.edu.bjtu.ebosmqrouter.service.MqFactory;
import cn.edu.bjtu.ebosmqrouter.service.impl.ActiveMqConsumerImpl;
import com.alibaba.fastjson.JSONObject;

import cn.edu.bjtu.ebosmqrouter.controller.MessageRouterController;
import cn.edu.bjtu.ebosmqrouter.service.MqProducer;
import cn.edu.bjtu.ebosmqrouter.util.ApplicationContextProvider;
import org.apache.activemq.command.*;
import org.apache.activemq.util.ByteSequence;

import javax.jms.*;


public class EdgexReadings implements Runnable {

    private String name;
    private String incomingQueue;
    private String outgoingQueue;
    private ConnectionFactory connectionFactory = ActiveMqConsumerImpl.connectionFactory;
    private MqFactory mqFactory = ApplicationContextProvider.getBean(MqFactory.class);
    private MqProducer mqProducer = mqFactory.createProducer();

    public EdgexReadings(String name, String incomingQueue, String outgoingQueue) {
        this.name = name;
        this.incomingQueue = incomingQueue;
        this.outgoingQueue = outgoingQueue;
    }

    public JSONObject getInfo(){
        JSONObject info = new JSONObject();
        info.put("name",this.name);
        info.put("incomingQueue",this.incomingQueue);
        info.put("outgoingQueue",this.outgoingQueue);
        return info;
    }

    @Override
    public void run() {
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(this.incomingQueue);
            MessageConsumer consumer = session.createConsumer(destination);
            while (MessageRouterController.check(name)) {
                try {
                    ActiveMQBytesMessage activeMQMessage = (ActiveMQBytesMessage) consumer.receive();
                    ByteSequence content = activeMQMessage.getContent();
                    String str = new String(content.getData());
                    JSONObject msg = JSONObject.parseObject(str);
                    System.out.println("收到"+destination+msg);
                    //TO DO CACULATION HERE

                    mqProducer.publish(this.outgoingQueue,str);

                }catch (Exception e){e.printStackTrace();break;}
            }
            connection.close();
        }catch (Exception e){e.printStackTrace();}
    }
}