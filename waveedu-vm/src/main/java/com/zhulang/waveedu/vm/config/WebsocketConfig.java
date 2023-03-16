package com.zhulang.waveedu.vm.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author 狐狸半面添
 * @create 2023-03-16 21:34
 */


@Configuration
public class WebsocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        // 注入ServerEndpointExporter，自动注册使用@ServerEndpoint注解的
        return new ServerEndpointExporter();
    }
}
