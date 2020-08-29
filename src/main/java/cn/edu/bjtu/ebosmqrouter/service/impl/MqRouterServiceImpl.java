package cn.edu.bjtu.ebosmqrouter.service.impl;

import cn.edu.bjtu.ebosmqrouter.entity.MqRouter;
import cn.edu.bjtu.ebosmqrouter.service.MqRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MqRouterServiceImpl implements MqRouterService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(String name, String incomingQueue, String outgoingQueue){
        MqRouter mqRouter = new MqRouter();
        mqRouter.setName(name);
        mqRouter.setIncomingQueue(incomingQueue);
        mqRouter.setOutgoingQueue(outgoingQueue);
        mqRouter.setCreated(new Date());
        mongoTemplate.save(mqRouter);
    }

    @Override
    public void delete(String name){
        Query query = Query.query(Criteria.where("name").is(name));
        mongoTemplate.remove(query,MqRouter.class,"RawRouter");
    }

    @Override
    public List<MqRouter> findAll() {
        return mongoTemplate.findAll(MqRouter.class,"RawRouter");
    }

}
