package com.xjdzy.config;

import com.xjdzy.mapper.ChatRecordsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 用于注入ServerEndpoint bean对象，Springboot初始化时就可以扫描使用@ServerEndpoint注解的类并为其创建对象
 */

@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
