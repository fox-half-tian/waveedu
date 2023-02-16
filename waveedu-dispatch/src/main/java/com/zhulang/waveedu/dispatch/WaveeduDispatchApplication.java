package com.zhulang.waveedu.dispatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 狐狸半面添
 * @create 2023-02-17 1:20
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class WaveeduDispatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaveeduDispatchApplication.class, args);
    }

}
