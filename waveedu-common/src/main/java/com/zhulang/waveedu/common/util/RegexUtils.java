package com.zhulang.waveedu.common.util;

import cn.hutool.core.util.StrUtil;

/**
 * 校验格式工具类
 *
 * @author 狐狸半面添
 * @create 2023-01-16 22:17
 */
public class RegexUtils {

    /**
     * 正则表达式模板
     *
     * @author 狐狸半面添
     * @create 2023-01-16 22:39
     */
    private static class RegexPatterns {
        /**
         * 手机号正则
         */
        public static final String PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
        /**
         * 邮箱正则
         */
        public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        /**
         * 密码正则。8~16位的字母、数字、下划线
         */
        public static final String PASSWORD_REGEX = "^\\w{8,16}$";
        /**
         * 验证码正则, 6位数字
         */
        public static final String VERIFY_CODE_REGEX = "^\\d{6}$";
    }

    /**
     * 是否是无效手机格式
     *
     * @param phone 要校验的手机号
     * @return true:符合，false：不符合
     */
    public static boolean isPhoneInvalid(String phone) {
        return mismatch(phone, RegexPatterns.PHONE_REGEX);
    }

    /**
     * 是否是无效邮箱格式
     *
     * @param email 要校验的邮箱
     * @return true:符合，false：不符合
     */
    public static boolean isEmailInvalid(String email) {
        return mismatch(email, RegexPatterns.EMAIL_REGEX);
    }

    /**
     * 是否是无效验证码格式
     *
     * @param code 要校验的验证码
     * @return true:符合，false：不符合
     */
    public static boolean isCodeInvalid(String code) {
        return mismatch(code, RegexPatterns.VERIFY_CODE_REGEX);
    }

    /**
     * 校验是否不符合正则格式
     *
     * @param str   字符串
     * @param regex 正则表达式
     * @return true:符合  false:不符合
     */
    private static boolean mismatch(String str, String regex) {
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }
}