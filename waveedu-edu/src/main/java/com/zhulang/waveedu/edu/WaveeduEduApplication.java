package com.zhulang.waveedu.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 狐狸半面添
 * @create 2023-02-03 0:11
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class WaveeduEduApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaveeduEduApplication.class, args);
    }
}
