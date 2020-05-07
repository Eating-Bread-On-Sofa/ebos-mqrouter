package cn.edu.bjtu.ebosmqrouter.util.dataAnalysis;

import cn.edu.bjtu.ebosmqrouter.service.MqConsumer;
import cn.edu.bjtu.ebosmqrouter.service.MqFactory;
import com.alibaba.fastjson.JSONObject;
import cn.edu.bjtu.ebosmqrouter.controller.MessageRouterController;
import cn.edu.bjtu.ebosmqrouter.service.MqProducer;
import cn.edu.bjtu.ebosmqrouter.util.ApplicationContextProvider;


public class Raw implements Runnable {

    private String name;
    private String incomingQueue;
    private String outgoingQueue;
    private MqFactory mqFactory = ApplicationContextProvider.getBean(MqFactory.class);

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
            MqProducer mqProducer = mqFactory.createProducer();
            MqConsumer mqConsumer = mqFactory.createConsumer(incomingQueue);
            while (MessageRouterController.check(name)) {
                try {
                    String msg = mqConsumer.subscribe();
                    System.out.println("收到"+incomingQueue+msg);
                    //TO DO CACULATION HERE
                    mqProducer.publish(this.outgoingQueue,msg);

                }catch (Exception e){e.printStackTrace();break;}
            }
        }catch (Exception e){e.printStackTrace();}
    }

}
