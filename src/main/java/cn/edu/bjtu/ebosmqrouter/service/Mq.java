package cn.edu.bjtu.ebosmqrouter.service;

public interface Mq {
    void publish(String topic, String message);
    String getMessage(String topic);
}
