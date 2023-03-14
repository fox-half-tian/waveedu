package com.zhulang.waveedu.judge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 狐狸半面添
 * @since 2023-03-12
 * @Description: 判题机服务系统启动类
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableAsync(proxyTargetClass=true) //开启异步注解
@EnableTransactionManagement
@EnableRetry
public class JudgeServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JudgeServerApplication.class,args);
    }
}