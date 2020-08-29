package cn.edu.bjtu.ebosmqrouter.service;

import cn.edu.bjtu.ebosmqrouter.entity.MqRouter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MqRouterService {
    void save(String name, String incomingQueue, String outgoingQueue);
    void delete(String name);
    List<MqRouter> findAll();
}
