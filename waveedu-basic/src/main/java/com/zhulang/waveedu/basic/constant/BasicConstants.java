package com.zhulang.waveedu.basic.constant;

/**
 * @author 狐狸半面添
 * @create 2023-01-19 0:15
 */
public class BasicConstants {
    /**
     * 默认的用户头像
     */
    public final static String DEFAULT_USER_ICON = "https://waveedu.oss-cn-hangzhou.aliyuncs.com/head-image/waveedudefault.png";
    /**
     * 登录时最大验证密码的次数
     */
    public static final int LOGIN_MAX_VERIFY_PWD_COUNT = 8;
    /**
     * 登录时最大验证code的次数
     */
    public static final int LOGIN_MAX_VERIFY_CODE_COUNT = 7;

    /**
     * 登录时当剩1 - 3次机会时返回会显示剩余次数
     */
    public static final int LOGIN_PWD_MAX_SURPLUS_COUNT = 3;
    /**
     * 注销时最大验证code的次数
     */
    public static final int LOGOFF_MAX_VERIFY_CODE_COUNT = 7;

    /**
     * 注销冻结天数，7天后将无法通过登录修改状态（真正注销）
     */
    public static final int LOGOFF_FROZEN_DAY = 7;
}
