package com.zhulang.waveedu.sms.config;

import com.aliyun.oss.OSS;
import com.zhulang.waveedu.sms.util.OssClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-01-29 1:41
 */
@Configuration
public class OssConfig {
    @Resource
    private OSS ossClient;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Bean
    public OssClientUtils ossClientUtils() {
        return new OssClientUtils(ossClient, accessId, endpoint, bucket);
    }


}
