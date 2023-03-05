package com.zhulang.waveedu.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class WaveeduServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaveeduServiceApplication.class, args);
    }
}
