package cn.edu.bjtu.ebosmqrouter.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class Init implements ApplicationRunner {
    @Value("${mq}")
    private String name;
    public static String mqname;

    public void run(ApplicationArguments arguments){
        mqname = name;
    }
}
