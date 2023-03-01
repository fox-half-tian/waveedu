package com.zhulang.waveedu.dispatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解决跨域问题
 *
 * @author 狐狸半面添
 * @create 2023-01-15 23:08
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer, Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");

        filterChain.doFilter(request, response);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .maxAge(3600);
//    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // 允许所有路径的请求
//        registry.addMapping("/**")
//                // 允许所有域名的请求
//                .allowedOrigins("*")
//                // 允许所有方法的请求
//                .allowedMethods("*")
//                // 允许所有头部的请求
//                .allowedHeaders("*");
//    }
}
