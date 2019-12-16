package cn.edu.bjtu.ebosmqrouter.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.edu.bjtu.ebosmqrouter.service.MqService;
import cn.edu.bjtu.ebosmqrouter.util.LayuiTableResultUtil;
import cn.edu.bjtu.ebosmqrouter.util.dataAnalysis.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.jms.*;
import java.util.Date;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RequestMapping("/api/message")
@RestController
public class MessageRouterController {
    @Autowired
    MqService mqService;
    public static JSONArray status = new JSONArray();
    public static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,50,3, TimeUnit.SECONDS,new SynchronousQueue<>());

    @PostMapping("/analysis/raw")
    @ResponseBody
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

    @PostMapping("/analysis/edgexreadings")
    @ResponseBody
    public String newEdgexReadings(@RequestBody JSONObject info){
        info.put("createTime", new Date().toString());
        if(!MessageRouterController.existed(info.getString("name"))){
            try{
                EdgexReadings edgexReadings = new EdgexReadings(info.getString("name"),info.getString("incomingQueue"),info.getString("outgoingQueue"));
                status.add(info);
                threadPoolExecutor.execute(edgexReadings);
                return "启动成功~~";}catch (Exception e){return "参数错误!";}
        }else {
            return "名称重复！";
        }
    }

    @GetMapping("/analysis")
    @ResponseBody
    public JSONArray allInfo(){
        return status;
    }

    @GetMapping("/analysis/table")
    @ResponseBody
    public LayuiTableResultUtil<JSONArray> allInfoTable(@RequestParam Integer page, @RequestParam Integer limit){
        LayuiTableResultUtil<JSONArray> table = new LayuiTableResultUtil<>("",status,0,status.size());
        return table;
    }

    @DeleteMapping("/analysis")
    @ResponseBody
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

    public static boolean existed(String name){
        boolean existed = false;
        for(int i=0; i<status.size(); i++){
            if(name.equals(status.getJSONObject(i).getString("name"))){existed = true;}
        }
        return existed;
    }
}
