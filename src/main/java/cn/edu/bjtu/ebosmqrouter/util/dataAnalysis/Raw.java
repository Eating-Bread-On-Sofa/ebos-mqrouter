package cn.edu.bjtu.ebosmqrouter.util.dataAnalysis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import cn.edu.bjtu.ebosmqrouter.controller.MessageRouterController;
import cn.edu.bjtu.ebosmqrouter.service.MqService;
import cn.edu.bjtu.ebosmqrouter.util.ApplicationContextProvider;
import org.apache.activemq.command.ActiveMQMapMessage;

import javax.jms.*;
import java.util.Map;

public class Raw implements Runnable {

    private String name;
    private String incomingQueue;
    private String outgoingQueue;
    private ConnectionFactory connectionFactory = MessageRouterController.connectionFactory;
    private MqService mqService = ApplicationContextProvider.getBean(MqService.class);

    public Raw(String name, String incomingQueue, String outgoingQueue) {
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
                    ActiveMQMapMessage activeMQMapMessage = (ActiveMQMapMessage) consumer.receive();
                    Map content = activeMQMapMessage.getContentMap();
                    JSONObject msg = new JSONObject(content);
                    System.out.println("收到"+destination+msg);
                    //TO DO CACULATION HERE

                    mqService.publish(this.outgoingQueue,msg);

                }catch (Exception e){e.printStackTrace();break;}
            }
            connection.close();
        }catch (Exception e){e.printStackTrace();}
    }

}
