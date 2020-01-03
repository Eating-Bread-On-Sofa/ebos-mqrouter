package cn.edu.bjtu.ebosmqrouter.component;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class MqListener implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments arguments){
        new Thread(() -> {
            int i = 0;
            while (true) {
                i++;
                try {
                    Thread.sleep(10000);
                    System.out.println("过去了10秒钟……,i的值为：" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == 4) { //第40秒时抛出一个异常
                    throw new RuntimeException();
                }
                continue;
            }
        }).start();
    }
}
