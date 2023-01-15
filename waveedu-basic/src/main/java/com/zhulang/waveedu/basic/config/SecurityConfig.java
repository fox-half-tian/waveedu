package com.zhulang.waveedu.basic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 与安全框架spring-security相关的
 *
 * @author 狐狸半面添
 * @create 2023-01-15 22:56
 */
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//
//    @Autowired
//    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
//    @Autowired
//    private AuthenticationEntryPointImpl authenticationEntryPoint;
//    @Autowired
//    private AccessDeniedHandlerImpl accessDeniedHandler;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                //关闭csrf
//                .csrf().disable()
//                //不通过Session获取SecurityContext
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                // 对于登录接口 允许匿名访问
//                .antMatchers(UrlUntil.LOGIN_URL, UrlUntil.REGISTER_URL, UrlUntil.FORGOT_PASSWORD,UrlUntil.GENERATE_CODE, UrlUntil.ADDQQ,"/druid/**", "/User/generateCode", "/User/addQQ", "/Test/test").permitAll()
////                .antMatchers("/testCors").hasAuthority("system:dept:list222")
//                // 除上面外的所有请求全部需要鉴权认证
//                .anyRequest().authenticated();
//
//        //添加过滤器
//        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//        //添加异常处理器
//        http.exceptionHandling()
//                //添加认证失败处理器
//                .authenticationEntryPoint(authenticationEntryPoint)
//                //添加授权失败处理器
//                .accessDeniedHandler(accessDeniedHandler);
//
//        http.cors();
//
//    }
//
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//}
