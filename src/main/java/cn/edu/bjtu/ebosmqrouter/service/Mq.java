package cn.edu.bjtu.ebosmqrouter.service;


public interface Mq {
    void publish(String destinationName, String message);
}
