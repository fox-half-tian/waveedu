package com.zhulang.waveedu.dispatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题
 *
 * @author 狐狸半面添
 * @create 2023-01-15 23:08
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .maxAge(3600);
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有路径的请求
                .allowedOrigins("*") // 允许所有域名的请求
                .allowedMethods("*") // 允许所有方法的请求
                .allowedHeaders("*"); // 允许所有头部的请求
    }
}
