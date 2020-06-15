package cn.edu.bjtu.ebosmqrouter.controller;

import cn.edu.bjtu.ebosmqrouter.service.RawRouter;
import cn.edu.bjtu.ebosmqrouter.service.LogService;
import cn.edu.bjtu.ebosmqrouter.service.MqFactory;
import cn.edu.bjtu.ebosmqrouter.service.MqProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
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
    private static final List<RawRouter> status = new LinkedList<>();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 50,3, TimeUnit.SECONDS,new SynchronousQueue<>());

    @GetMapping("/test/{topic}/{msg}")
    public String test(@PathVariable String topic, @PathVariable String msg){
        MqProducer mqProducer = mqFactory.createProducer();
        mqProducer.publish(topic,msg);
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
                status.add(rawRouter);
                threadPoolExecutor.execute(rawRouter);
                logService.info(null,"添加新路由" + rawRouter.toString());
                return "启动成功";
            } catch (Exception e) {
                e.printStackTrace();
                return "参数错误!";
            }
        } else {
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
        }
        logService.info(null,"删除路由"+name+":"+flag);
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

    @ApiOperation(value = "微服务健康监测")
    @CrossOrigin
    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }
}
