package cn.edu.bjtu.ebosmqrouter.controller;

import cn.edu.bjtu.ebosmqrouter.service.Log;
import cn.edu.bjtu.ebosmqrouter.service.LogFind;
import cn.edu.bjtu.ebosmqrouter.service.MqFactory;
import cn.edu.bjtu.ebosmqrouter.service.impl.LogFindImpl;
import cn.edu.bjtu.ebosmqrouter.service.log.LogImpl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.edu.bjtu.ebosmqrouter.service.MqProducer;
import cn.edu.bjtu.ebosmqrouter.util.LayuiTableResultUtil;
import cn.edu.bjtu.ebosmqrouter.util.dataAnalysis.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RequestMapping("/api/router")
@RestController
public class MessageRouterController {
    @Autowired
    MqFactory mqFactory;
    @Autowired
    Log log = new LogImpl();
    @Autowired
    LogFind logFind = new LogFindImpl();
    public static JSONArray status = new JSONArray();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,50,3, TimeUnit.SECONDS,new SynchronousQueue<>());

    @GetMapping("/test/{topic}")
    public String test(@PathVariable String topic, @RequestBody String content){
        MqProducer mqProducer = mqFactory.createProducer();
        mqProducer.publish(topic,content);
        return mqProducer.getClass().toString();
    }

    @CrossOrigin
    @PostMapping("/raw")
    public String newRaw(@RequestBody JSONObject info){
        info.put("createTime", new Date().toString());
        if(!MessageRouterController.existed(info.getString("name"))){
            try{
            Raw raw = new Raw(info.getString("name"),info.getString("incomingQueue"),info.getString("outgoingQueue"));
            status.add(info);
            threadPoolExecutor.execute(raw);
            return "启动成功~~";}catch (Exception e){return "参数错误!";}
        }else {
            return "名称重复！";
        }
    }


    @CrossOrigin
    @GetMapping()
    public JSONArray allInfo(){
        return status;
    }

    @CrossOrigin
    @GetMapping("/list")
    public LayuiTableResultUtil<JSONArray> allInfoTable(){
        LayuiTableResultUtil<JSONArray> table = new LayuiTableResultUtil<>("",status,0,status.size());
        return table;
    }

    @CrossOrigin
    @DeleteMapping()
    public boolean delete(@RequestBody JSONObject info){
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
    @RequestMapping ("/logtest")
    public String logtest1(){
        log.error("fail");
        log.debug("mqrouter");
        return "成功";
    }
    @CrossOrigin
    @GetMapping("/logtest")
    public String logtest2(){
        return logFind.readAll();
    }
}
