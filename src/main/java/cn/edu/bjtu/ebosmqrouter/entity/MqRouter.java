package cn.edu.bjtu.ebosmqrouter.entity;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "RawRouter")
public class MqRouter implements Serializable {
    private String name;
    private String incomingQueue;
    private String outgoingQueue;
    @ApiModelProperty(example = "2020-01-01 12:12:12")
    private Date created;

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
