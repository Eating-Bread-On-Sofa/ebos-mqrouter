package cn.edu.bjtu.ebosmqrouter.service;

public interface MqFactory {
    MqProducer createProducer();
    MqConsumer createConsumer(String topic);
}
