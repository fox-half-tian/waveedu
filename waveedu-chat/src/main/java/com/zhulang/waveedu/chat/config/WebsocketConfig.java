package com.zhulang.waveedu.chat.config;

/**
 * @author 阿东
 * @date 2023/3/4 [14:03]
 */
import com.zhulang.waveedu.chat.handler.MyWebSocketHandler;
import com.zhulang.waveedu.chat.interceptor.MyHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebsocketConfig /* implements WebSocketConfigurer */{
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        // 注入ServerEndpointExporter，自动注册使用@ServerEndpoint注解的
        return new ServerEndpointExporter();
    }

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        //配置处理器
//        registry.addHandler(this.myWebSocketHandler(), "/")
//                //配置拦截器
//                .addInterceptors(new MyHandshakeInterceptor())
//                .setAllowedOrigins("*");
//    }
//
//    @Bean
//    public WebSocketHandler myWebSocketHandler() {
//        return new MyWebSocketHandler();
//    }

    @Bean
    public MyHandshakeInterceptor webSocketShakeInterceptor() {
        return new MyHandshakeInterceptor();
    }

}
