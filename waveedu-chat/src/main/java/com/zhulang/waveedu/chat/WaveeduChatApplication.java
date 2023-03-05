package com.zhulang.waveedu.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 狐狸半面添
 * @create 2023-03-01 19:08
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class WaveeduChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaveeduChatApplication.class);
    }
}
