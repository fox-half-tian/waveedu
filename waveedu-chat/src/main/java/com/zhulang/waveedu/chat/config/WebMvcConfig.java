package com.zhulang.waveedu.chat.config;

import com.zhulang.waveedu.chat.filter.CorsFilter;
import com.zhulang.waveedu.chat.interceptor.RemoveInterceptor;
import com.zhulang.waveedu.common.mapper.JacksonObjectMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-02-11 23:25
 */

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport   {

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0, messageConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(new RemoveInterceptor());
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .maxAge(3600);
//    }

    // 注入FilterRegistrationBean对象
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
        // 设置filter属性为自定义跨域过滤器的实例
        bean.setFilter(new CorsFilter());
        // 设置order属性为一个整数值，表示过滤器的优先级
        bean.setOrder(0);
        return bean;
    }
}
