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
     * 用户登录时，只允许该用户的单线程操作，防止恶意并发尝试
     * TTL为 10s
     */
    public static final String LOCK_LOGIN_USER_KEY = "lock:login:user:";
    public static final Long LOCK_LOGIN_USER_CODE_TTL = 10L;

    /**
     * 管理员登录时，只允许该用户的单线程操作，防止恶意并发尝试
     * TTL为 10s
     */
    public static final String LOCK_LOGIN_ADMIN_KEY = "lock:login:admin:";
    public static final Long LOCK_LOGIN_ADMIN_CODE_TTL = 10L;


    /**
     * 用户登录后保存在redis的信息
     * 有效期：五小时
     * 如果离过期还有4小时就刷新一次有效期
     */
    public static final String LOGIN_USER_INFO_KEY = "login:user:info:";
    public static final Long LOGIN_USER_INFO_TTL = 60 * 60 * 5L;
    public static final Long LOGIN_USER_INFO_REFRESH_TTL = 60 * 60 * 4L;

    /**
     * 管理员登录后保存在redis的信息
     * 有效期：五小时
     * 如果离过期还有4小时就刷新一次有效期
     */
    public static final String LOGIN_ADMIN_INFO_KEY = "login:admin:info:";
    public static final Long LOGIN_ADMIN_INFO_TTL = 60 * 60 * 5L;
    public static final Long LOGIN_ADMIN_INFO_REFRESH_TTL = 60 * 60 * 4L;

    /**
     * 保存用户通过密码登录的验证次数
     * 有效期：2分钟
     * 2分钟内验证次数达到8次依旧错误，将会冻结手机号以密码方式登录
     */
    public static final String LOGIN_USER_PWD_KEY = "login:user:pwd:";
    public static final Long LOGIN_USER_PWD_TTL = 60 * 2L;
    public static final Long LOGIN_USER_PWD_LOCK_TTL = 60 * 15L;

    /**
     * 保存管理员通过密码登录的验证次数
     * 有效期：2分钟
     * 2分钟内验证次数达到8次依旧错误，将会冻结登录
     */
    public static final String LOGIN_ADMIN_PWD_KEY = "login:admin:";
    public static final Long LOGIN_ADMIN_PWD_TTL = 60 * 2L;
    public static final Long LOGIN_ADMIN_PWD_LOCK_TTL = 60 * 15L;

    /**
     * 用户注销手机验证码
     * 有效期：5分钟
     * 剩余时长大于 4分钟 则无法再次发送
     */
    public static final String LOGOFF_USER_CODE_KEY = "logoff:user:code:";
    public static final Long LOGOFF_USER_CODE_TTL = 60 * 5L;
    public static final Long LOGOFF_USER_CODE_AGAIN_TTL = 60 * 4L;

    /**
     * 用户注销时，只允许该用户的单线程操作，防止恶意并发尝试
     * TTL为 10s
     */
    public static final String LOCK_LOGOFF_USER_KEY = "lock:logoff:user:";
    public static final Long LOCK_LOGOFF_USER_CODE_TTL = 10L;

    /**
     * 修改密码时图片验证码字符缓存
     * TTL为 2min
     */
    public static final String PWD_CODE_KEY = "pwd:code:";
    public static final Long PWD_CODE_TTL = 60 * 2L;
    /**
     * 课程信息
     * ttl为 10 min
     */
    public static final String LESSON_INFO_KEY = "lesson:info:";
    public static final Long LESSON_INFO_TTL = 60 * 10L;

    /**
     * 修改普通作业的总分数时，只允许该用户的单线程操作，防止并发导致总分数改变
     * TTL为 10s
     */
    public static final String LOCK_COMMON_HOMEWORK_JUDGE_STU_COMMIT_KEY = "lock:commonHomework:stuCommit:";
    public static final Long LOCK_COMMON_HOMEWORK_JUDGE_STU_COMMIT_TTL = 10L;


    /**
     * 班级群聊的所有班级的key
     */
    public static final String CHAT_CLASS_INFO = "chat:class:info:";

    /**
     * 用户判题时，只允许该用户的单线程操作，减轻判题机压力
     * TTL为 10s
     */
    public static final String LOCK_JUDGE_KEY = "lock:judge:";
    public static final Long LOCK_JUDGE_TTL = 20L;

}
