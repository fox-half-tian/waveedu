package com.zhulang.waveedu.basic.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 与ORM框架mybatis-plus相关的配置
 *
 * @author 狐狸半面添
 * @create 2023-01-15 22:40
 */
@Configuration
@EnableTransactionManagement //开启事务
@MapperScan("com.zhulang.waveedu.basic.dao")
public class MyBatisConfig {

}
