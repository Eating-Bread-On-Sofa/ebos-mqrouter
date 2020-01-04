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

        }).start();
    }
}
