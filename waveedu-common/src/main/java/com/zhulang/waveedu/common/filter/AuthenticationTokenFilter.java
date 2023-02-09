package com.zhulang.waveedu.common.filter;


import com.zhulang.waveedu.common.constant.CommonConstants;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.RedisUser;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import com.zhulang.waveedu.common.util.WebUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 20:59
 */
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private RedisCacheUtils redisCacheUtils;

    public AuthenticationTokenFilter(RedisCacheUtils redisCacheUtils) {
        this.redisCacheUtils = redisCacheUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader(CommonConstants.REQUEST_HEADER_TOKEN);
        if (!StringUtils.hasText(token)) {
            // 没有 token 则放行
            filterChain.doFilter(request, response);
            return;
        }

        // 解析token
        String decrypt = CipherUtils.decrypt(token);
        if (decrypt == null) {
            WebUtils.renderString(response, Result.error(HttpStatus.HTTP_ILLEGAL_OPERATION.getCode(), HttpStatus.HTTP_ILLEGAL_OPERATION.getValue()));
            return;
        }
        String[] info = WaveStrUtils.strSplitToArr(decrypt, "-");
        if (info.length != 2) {
            WebUtils.renderString(response, Result.error(HttpStatus.HTTP_ILLEGAL_OPERATION.getCode(), HttpStatus.HTTP_ILLEGAL_OPERATION.getValue()));
            return;
        }
        // 从redis中获取用户信息
        // info[0]是id，info[1]是uuid
        RedisUser redisUser = redisCacheUtils.getCacheObject(RedisConstants.LOGIN_USER_INFO_KEY + info[0]);
        if (Objects.isNull(redisUser)) {
            // redis中无该用户id的缓存信息
            WebUtils.renderString(response, Result.error(HttpStatus.HTTP_LOGIN_EXPIRE.getCode(), HttpStatus.HTTP_LOGIN_EXPIRE.getValue()));
            return;
        }
        if (!redisUser.getUuid().equals(info[1])) {
            // token未过期，但token值不一样 --> 在其它地方登录了该用户
            WebUtils.renderString(response, Result.error(HttpStatus.HTTP_USER_CROWDING.getCode(), HttpStatus.HTTP_USER_CROWDING.getValue()));
            return;
        }

        // 说明token有效且未过期

        /*
            (System.currentTimeMillis() - redisUser.getTime())/1000：上一次设置ttl距离现在的时长，这个时差肯定没有超过LOGIN_USER_INFO_TTL
            RedisConstants.LOGIN_USER_INFO_TTL - (System.currentTimeMillis() - redisUser.getTime()/1000)：距离过期还有多久
         */
        if (RedisConstants.LOGIN_USER_INFO_TTL - (System.currentTimeMillis() - redisUser.getTime()) / 1000 <= RedisConstants.LOGIN_USER_INFO_REFRESH_TTL) {
            // 距离过期90分钟不到，就刷新缓存
            redisUser.setTime(System.currentTimeMillis());
            redisCacheUtils.setCacheObject(RedisConstants.LOGIN_USER_INFO_KEY + info[0], redisUser, RedisConstants.LOGIN_USER_INFO_TTL);
        }

        // 存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(redisUser, null, redisUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }
}
