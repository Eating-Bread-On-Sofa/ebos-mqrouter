package cn.edu.bjtu.ebosmqrouter.service;

import cn.edu.bjtu.ebosmqrouter.controller.MessageRouterController;
import cn.edu.bjtu.ebosmqrouter.util.ApplicationContextProvider;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class RawRouter implements Runnable {

    private String name;
    private String incomingQueue;
    private String outgoingQueue;
    @ApiModelProperty(example = "2020-01-01 12:12:12")
    private Date created;
    private MqFactory mqFactory = ApplicationContextProvider.getBean(MqFactory.class);

    public RawRouter(String name, String incomingQueue, String outgoingQueue, Date created) {
        this.name = name;
        this.incomingQueue = incomingQueue;
        this.outgoingQueue = outgoingQueue;
        if(created == null){
            this.created = new Date();
        }else{
            this.created = created;
        }
    }

    @Override
    public void run() {
        try {
            MqProducer mqProducer = mqFactory.createProducer();
            MqConsumer mqConsumer = mqFactory.createConsumer(incomingQueue);
            while (true) {
                try {
                    String msg = mqConsumer.subscribe();
                    if(!MessageRouterController.check(name)){
                        break;
                    }
                    System.out.println("收到"+incomingQueue+msg);
                    //TO DO CACULATION HERE
                    mqProducer.publish(this.outgoingQueue,msg);

                }catch (Exception e){e.printStackTrace();break;}
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIncomingQueue() {
        return incomingQueue;
    }

    public void setIncomingQueue(String incomingQueue) {
        this.incomingQueue = incomingQueue;
    }

    public String getOutgoingQueue() {
        return outgoingQueue;
    }

    public void setOutgoingQueue(String outgoingQueue) {
        this.outgoingQueue = outgoingQueue;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
