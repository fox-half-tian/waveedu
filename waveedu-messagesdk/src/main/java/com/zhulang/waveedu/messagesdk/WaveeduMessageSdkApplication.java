package com.zhulang.waveedu.messagesdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 狐狸半面添
 * @create 2023-02-28 20:48
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class WaveeduMessageSdkApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaveeduMessageSdkApplication.class);
    }
}
