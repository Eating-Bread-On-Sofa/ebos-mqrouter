package cn.edu.bjtu.ebosmqrouter.controller;

import cn.edu.bjtu.ebosmqrouter.service.LogService;
import cn.edu.bjtu.ebosmqrouter.service.MqFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.edu.bjtu.ebosmqrouter.service.MqProducer;
import cn.edu.bjtu.ebosmqrouter.util.dataAnalysis.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
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
    public static JSONArray status = new JSONArray();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,50,3, TimeUnit.SECONDS,new SynchronousQueue<>());

    @GetMapping("/test/{topic}")
    public String test(@PathVariable String topic, @RequestBody String content){
        MqProducer mqProducer = mqFactory.createProducer();
        mqProducer.publish(topic,content);
        return mqProducer.getClass().toString();
    }

    @ApiOperation(value = "添加消息路由",notes = "需name，incomingQueue，outcomingQueue三个字段的JSON对象，分别为路由名称、接收消息队列名称、转发消息队列名称")
    @ApiImplicitParam(name = "info",value = "路由信息",required = true,dataType = "JSONObject")
    @CrossOrigin
    @PostMapping()
    public String newRouter(@RequestBody JSONObject info){
        info.put("createTime", new Date().toString());
        if(!MessageRouterController.existed(info.getString("name"))){
            try{
            Raw raw = new Raw(info.getString("name"),info.getString("incomingQueue"),info.getString("outgoingQueue"));
            status.add(info);
            threadPoolExecutor.execute(raw);
            logService.info("添加新路由"+info.toString());
            return "启动成功";}catch (Exception e){return "参数错误!";}
        }else {
            return "名称重复！";
        }
    }

    @ApiOperation(value = "查看消息路由",notes = "返回JSON Array，每个Object为一个路由")
    @CrossOrigin
    @GetMapping()
    public JSONArray allInfo(){
        return status;
    }

    @ApiOperation(value = "删除消息路由",notes = "需和GET方法中的信息相同，包括name，incomingQueue，outcomingQueue，createTime")
    @ApiImplicitParam(name = "info",value = "路由信息",required = true,dataType = "JSONObject")
    @CrossOrigin
    @DeleteMapping()
    public boolean delete(@RequestBody JSONObject info){
        logService.info("删除路由"+info.toString());
        return status.remove(info);
    }

    public static boolean check(String name){
        boolean flag = false;
        for (int i = 0; i < status.size(); i++){
            if(name.equals(status.getJSONObject(i).getString("name"))){
                flag = true;
            }
        }
        return flag;
    }

    private static boolean existed(String name){
        boolean existed = false;
        for(int i=0; i<status.size(); i++){
            if(name.equals(status.getJSONObject(i).getString("name"))){existed = true;}
        }
        return existed;
    }

    @CrossOrigin
    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    @CrossOrigin
    @GetMapping("/log")
    public String getLog(){
        return logService.findAll();
    }
}
