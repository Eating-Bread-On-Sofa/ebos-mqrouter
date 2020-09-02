package cn.edu.bjtu.ebosmqrouter.controller;

import cn.edu.bjtu.ebosmqrouter.entity.Subscribe;
import cn.edu.bjtu.ebosmqrouter.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Api(tags = "消息路由")
@RequestMapping("/api/router")
@RestController
public class MessageRouterController {
    @Autowired
    MqFactory mqFactory;
    @Autowired
    LogService logService;
    @Autowired
    MqRouterService mqRouterService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    SubAndPubService subAndPubService;
    @Autowired
    RestTemplate restTemplate;

    public static final List<RawRouter> status = new LinkedList<>();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 50,3, TimeUnit.SECONDS,new SynchronousQueue<>());

    @GetMapping("/test/{topic}/{msg}")
    public String test(@PathVariable String topic, @PathVariable String msg){
        MqProducer mqProducer = mqFactory.createProducer();
        mqProducer.publish(topic,msg);
        logService.info("create","mqrouter微服务向" + topic + "发布一条消息：" + msg);
        return mqProducer.getClass().toString();
    }

    @ApiOperation(value = "添加消息路由",notes = "只需name，incomingQueue，outgoingQueue三个字段，" +
            "分别为路由名称、接收消息队列名称、转发消息队列名称，\n\n"+
            "created字段不用写，如果非要写一定要按示例写对，并且不会生效，如果写了还格式不对就会报错")
    @CrossOrigin
    @PostMapping()
    public String newRouter(@RequestBody RawRouter rawRouter) {
        if (!MessageRouterController.check(rawRouter.getName())) {
            try {
                mqRouterService.save(rawRouter.getName(),rawRouter.getIncomingQueue(),rawRouter.getOutgoingQueue());
                status.add(rawRouter);
                threadPoolExecutor.execute(rawRouter);
                logService.info("create","添加新路由：" + rawRouter.toString());
                return "启动成功";
            } catch (Exception e) {
                e.printStackTrace();
                logService.error("create","添加消息路由时，参数有误");
                return "参数错误!";
            }
        } else {
            logService.error("create","添加消息路由时，名称重复，添加无效！");
            return "名称重复！";
        }
    }

    @ApiOperation(value = "查看消息路由",notes = "返回JSON Array，每个Object为一个路由")
    @CrossOrigin
    @GetMapping()
    public List<RawRouter> allInfo(){
        return status;
    }

    @ApiOperation(value = "删除消息路由")
    @CrossOrigin
    @DeleteMapping("/name/{name}")
    public boolean delete(@PathVariable String name){
        boolean flag;
        synchronized (status){
            flag = status.remove(search(name));
            mqRouterService.delete(name);
        }
        logService.info("delete","删除路由"+name+":"+flag);
        return flag;
    }

    public static boolean check(String name){
        boolean flag = false;
        for (RawRouter rawRouter : status) {
            if(name.equals(rawRouter.getName())){
                flag=true;
                break;
            }
        }
        return flag;
    }

    public static RawRouter search(String name){
        for (RawRouter rawRouter : status) {
            if(name.equals(rawRouter.getName())){
                return rawRouter;
            }
        }
        return null;
    }

    @ApiOperation(value = "按微服务名称搜索订阅情况",notes = "返回一个集合")
    @CrossOrigin
    @GetMapping("/serviceName")
    public List<Subscribe> findByName(String serviceName){
        Query query = Query.query(Criteria.where("serviceName").is(serviceName));
        logService.info("retrieve","搜索微服务"+serviceName+"订阅的topic");
        return mongoTemplate.find(query,Subscribe.class,"subscribe");
    }

    @ApiOperation(value = "查看订阅情况",notes = "返回当前所有订阅信息")
    @CrossOrigin
    @GetMapping("/allSubscribe")
    public List<Subscribe> showAll(){
        return mongoTemplate.findAll(Subscribe.class,"subscribe");
    }

    @ApiOperation(value = "删除订阅主题",notes = "按照微服务名称和主题名称删除")
    @CrossOrigin
    @DeleteMapping("/subscribe")
    public String deleteSubtopic(String serviceName,String subTopic){
        Query query = Query.query(Criteria.where("serviceName").is(serviceName).and("subTopic").is(subTopic));
        mongoTemplate.remove(query, Subscribe.class, "subscribe");
        String url = subAndPubService.getSubUrl(serviceName) + "/" + subTopic;
        restTemplate.delete(url);
        logService.info("delete","删除微服务"+serviceName+"订阅的主题"+subTopic);
        return "删除成功";
    }

    @ApiOperation(value = "订阅主题",notes = "所有微服务动态订阅主题的接口")
    @CrossOrigin
    @PostMapping("/subscribe")
    public String subscribe(String serviceName,String subTopic){
        String url = subAndPubService.getSubUrl(serviceName);
        MultiValueMap<String,String> subscribe = new LinkedMultiValueMap<>();
        subscribe.add("subTopic",subTopic);
        String result = restTemplate.postForObject(url,subscribe,String.class);
        logService.info("create","微服务"+serviceName+"成功订阅主题"+subTopic);
        return result;
    }

    @ApiOperation(value = "发布消息",notes = "所有微服务向某topic发布消息的接口")
    @CrossOrigin
    @PostMapping("/publish")
    public String publish(String serviceName,String topic,String message){
        String url = subAndPubService.getPubUrl(serviceName);
        MultiValueMap<String,String> publish = new LinkedMultiValueMap<>();
        publish.add("topic",topic);
        publish.add("message",message);
        restTemplate.postForObject(url,publish,String.class);
        logService.info("create","微服务"+serviceName+"向主题"+topic+"成功发布一条消息:"+message);
        return "发布成功";
    }

    @ApiOperation(value = "微服务健康监测")
    @CrossOrigin
    @GetMapping("/ping")
    public String ping(){
        logService.info("retrieve","对mqroute微服务进行了一次健康检测");
        return "pong";
    }
}
