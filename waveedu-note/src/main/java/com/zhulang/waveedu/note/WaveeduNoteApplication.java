package com.zhulang.waveedu.note;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 狐狸半面添
 * @create 2023-03-01 19:08
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class WaveeduNoteApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaveeduNoteApplication.class);
    }
}
