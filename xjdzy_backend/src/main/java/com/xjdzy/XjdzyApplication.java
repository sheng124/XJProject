package com.xjdzy;

import com.xjdzy.websocket.CommunicationEndPoint;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com/xjdzy/mapper")
public class XjdzyApplication {

    public static void main(String[] args) {
        //SpringApplication.run(XjdzyApplication.class, args);
        // 解决在websocket中注入mapper问题
        SpringApplication springApplication = new SpringApplication(XjdzyApplication.class);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
        CommunicationEndPoint.setApplicationContext(configurableApplicationContext);
    }

}
