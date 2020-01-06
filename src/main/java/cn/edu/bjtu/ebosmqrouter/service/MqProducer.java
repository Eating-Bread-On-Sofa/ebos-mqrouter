package cn.edu.bjtu.ebosmqrouter.service;

public interface MqProducer {
    void publish(String topic, String message);
}
