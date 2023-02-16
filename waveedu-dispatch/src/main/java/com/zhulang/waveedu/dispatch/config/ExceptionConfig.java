package com.zhulang.waveedu.dispatch.config;

import com.zhulang.waveedu.common.exception.WaveEduExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 狐狸半面添
 * @create 2023-01-18 17:43
 */
@Configuration
public class ExceptionConfig {
    @Bean
    public WaveEduExceptionHandler waveEduExceptionHandler(){
        return new WaveEduExceptionHandler();
    }
}
