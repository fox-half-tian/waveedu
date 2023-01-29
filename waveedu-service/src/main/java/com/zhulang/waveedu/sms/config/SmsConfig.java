package com.zhulang.waveedu.sms.config;

import com.zhulang.waveedu.sms.util.SmsTemplateUtils;
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
    @ConfigurationProperties(prefix = "sms")
    public SmsTemplateUtils smsTemplateUtils(){
        return new SmsTemplateUtils();
    }
}
