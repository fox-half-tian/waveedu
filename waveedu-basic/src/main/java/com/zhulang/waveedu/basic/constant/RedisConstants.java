package com.zhulang.waveedu.basic.constant;

/**
 * 定义 redis 缓存的常量前缀
 *
 * @author 狐狸半面添
 * @create 2023-01-17 16:44
 */
public class RedisConstants {
    /**
     * 登录&注册手机验证码
     * 有效期：5分钟
     */
    public static final String LOGIN_CODE_KEY = "login:user:code:";
    public static final Long LOGIN_CODE_TTL = 5L;
}
