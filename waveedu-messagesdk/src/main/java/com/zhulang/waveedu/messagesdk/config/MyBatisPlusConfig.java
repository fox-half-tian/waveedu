package com.zhulang.waveedu.messagesdk.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zhulang.waveedu.common.handler.MyMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 与ORM框架mybatis-plus相关的配置
 * EnableTransactionManagement 开启事务
 *
 * @author 狐狸半面添
 * @create 2023-01-15 22:40
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.zhulang.waveedu.messagesdk.dao")
public class MyBatisPlusConfig {
    @Bean
    public MetaObjectHandler metaObjectHandler(){
        return new MyMetaObjectHandler();
    }
}
