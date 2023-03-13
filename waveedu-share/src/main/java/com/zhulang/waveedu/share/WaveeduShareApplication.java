package com.zhulang.waveedu.share;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 狐狸半面添
 * @create 2023-03-11 15:24
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class WaveeduShareApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaveeduShareApplication.class, args);
    }

}
