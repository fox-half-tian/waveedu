package com.zhulang.waveedu.basic.config;

import com.zhulang.waveedu.common.util.TxSmsTemplateUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 23:26
 */
public class SmsConfig {
    @Bean
    @ConfigurationProperties(prefix = "sms.tx")
    public TxSmsTemplateUtils smsTemplateUtils(){
        return new TxSmsTemplateUtils();
    }
}
