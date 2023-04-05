package com.zhulang.waveedu.service.config;

import com.zhulang.waveedu.service.util.AliSmsTemplateUtils;
import com.zhulang.waveedu.service.util.TxSmsTemplateUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 23:26
 */
@Configuration
public class SmsConfig {
    @Bean
    @ConfigurationProperties(prefix = "sms.tx")
    public TxSmsTemplateUtils smsTemplateUtils(){
        return new TxSmsTemplateUtils();
    }

    /**
     * 配置阿里短信发送工具类
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.ali")
    public AliSmsTemplateUtils aliSmsTemplateUtils(){
        return new AliSmsTemplateUtils();
    }
}
