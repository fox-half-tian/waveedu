package com.zhulang.waveedu.common.util;

import com.zhulang.waveedu.common.entity.RedisUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 狐狸半面添
 * @create 2023-01-18 1:35
 */
public class UserHolderUtils {
    /**
     * 通过前端传过来的token来获取当前登录的用户id
     *
     * @return 用户id
     */
    public static Long getUserId() {
        return ((RedisUser) ((SecurityContextHolder.getContext().getAuthentication())
                .getPrincipal()))
                .getId();
    }

    /**
     * 获取当前登录用户缓存在redis中的信息
     *
     * @return 用户基础信息
     */
    public static RedisUser getRedisUser(){
        return (RedisUser) (SecurityContextHolder.getContext().getAuthentication())
                .getPrincipal();
    }
}
