package cn.edu.bjtu.ebosmqrouter.service;

import cn.edu.bjtu.ebosmqrouter.controller.MessageRouterController;
import cn.edu.bjtu.ebosmqrouter.entity.MqRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class InitRawRouter implements ApplicationRunner {

    @Autowired
    MqRouterService mqRouterService;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 50,3, TimeUnit.SECONDS,new SynchronousQueue<>());

    @Override
    public void run(ApplicationArguments args) throws Exception{
        List<MqRouter> routers = mqRouterService.findAll();

        for (MqRouter router : routers){
            RawRouter rawRouter = new RawRouter(router.getName(),router.getIncomingQueue(),router.getOutgoingQueue(),router.getCreated());
            MessageRouterController.status.add(rawRouter);
            threadPoolExecutor.execute(rawRouter);
        }
    }
}
