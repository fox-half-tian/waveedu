package com.zhulang.waveedu.share.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.zhulang.waveedu.common.component.BatchSqlInjector;
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
@MapperScan("com.zhulang.waveedu.share.dao")
public class MyBatisPlusConfig {
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MyMetaObjectHandler();
    }

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory());
    }

    @Bean
    public BatchSqlInjector batchSqlInjector() {
        return new BatchSqlInjector();
    }
}
