package com.zhulang.waveedu.basic.filter;


import com.zhulang.waveedu.common.util.RedisCacheUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 20:59
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisCacheUtils redisCacheUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            // 没有 token 则放行
            filterChain.doFilter(request, response);
            return;
        }

        // 解析token
//        String userid;
//        try {
//            Claims claims = JwtUtils.parseJWT(token);
//            userid = claims.getSubject();
//        } catch (Exception e) {
//            throw new RuntimeException("登录过期，请重新登录");
//        }
//        //从redis中获取用户信息
//
//        if(Objects.isNull(loginUser)){
//            throw new RuntimeException("用户未登录");
//        }
//        //存入SecurityContextHolder
//        //TODO 获取权限信息封装到Authentication中
//        UsernamePasswordAuthenticationToken authenticationToken =
///               new UsernamePasswordAuthenticationToken(,null,loginUser.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }
}
