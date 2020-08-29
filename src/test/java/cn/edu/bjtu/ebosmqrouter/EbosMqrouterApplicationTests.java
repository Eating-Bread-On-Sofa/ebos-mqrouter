package cn.edu.bjtu.ebosmqrouter;

import cn.edu.bjtu.ebosmqrouter.service.MqFactory;
import cn.edu.bjtu.ebosmqrouter.service.MqProducer;
import cn.edu.bjtu.ebosmqrouter.util.ApplicationContextProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EbosMqrouterApplicationTests {

    @Test
    void routerTest() {
        MqFactory mqFactory = ApplicationContextProvider.getBean(MqFactory.class);
        MqProducer mqProducer = mqFactory.createProducer();
        mqProducer.publish("subQ1","！！！");

    }
}
