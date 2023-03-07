package com.zhulang.waveedu.chat.config;

/**
 * @author 阿东
 * @date 2023/3/4 [14:03]
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebsocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        // 注入ServerEndpointExporter，自动注册使用@ServerEndpoint注解的
        return new ServerEndpointExporter();
    }

}
