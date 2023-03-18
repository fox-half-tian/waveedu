package com.zhulang.waveedu.vm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 启动类
 * EnableAspectJAutoProxy 暴露代理对象
 *
 * @author 狐狸半面添
 * @create 2023-01-16 17:03
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class WaveeduVmApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaveeduVmApplication.class, args);
    }
}
