package com.zhulang.waveedu.common.constant;

/**
 * 定义 redis 缓存的常量前缀
 *
 * @author 狐狸半面添
 * @create 2023-01-17 16:44
 */
public class RedisConstants {
    /**
     * 用户登录&注册手机验证码
     * 有效期：5分钟
     * 剩余时长大于 4分钟 则无法再次发送
     */
    public static final String LOGIN_USER_CODE_KEY = "login:user:code:";
    public static final Long LOGIN_USER_CODE_TTL = 60 * 5L;
    public static final Long LOGIN_USER_CODE_AGAIN_TTL = 60 * 4L;

    /**
     * 用户使用验证码登录时，只允许该用户的单线程操作，防止恶意并发尝试
     * TTL为 10s
     */
    public static final String LOCK_LOGIN_USER_CODE_KEY = "lock:login:user:code:";
    public static final Long LOCK_LOGIN_USER_CODE_TTL = 10L;
    /**
     * 登录时最大验证code的次数
     */
    public static final int LOGIN_MAX_VERIFY_CODE_COUNT = 7;

    /**
     * 用户登录后保存在redis的信息
     * 有效期：两小时
     */
    public static final String LOGIN_USER_INFO_KEY = "login:user:info:";
    public static final Long LOGIN_USER_INFO_TTL = 60 * 60 * 2L;
}
