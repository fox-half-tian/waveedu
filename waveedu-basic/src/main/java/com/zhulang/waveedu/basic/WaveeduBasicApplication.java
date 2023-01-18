package com.zhulang.waveedu.basic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * 启动类
 *
 * @author 狐狸半面添
 * @create 2023-01-16 17:03
 */
@SpringBootApplication
@MapperScan("com.zhulang.waveedu.basic.dao")
public class WaveeduBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaveeduBasicApplication.class, args);
    }

}
