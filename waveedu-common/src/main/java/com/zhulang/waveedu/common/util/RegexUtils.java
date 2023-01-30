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
    public static class RegexPatterns {
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

        /**
         * uuid正则，32位0-9，a-f
         */
        public static final String UUID_REGEX = "^[0-9abcdef]{32}$";
        /**
         * 图片验证码字符正则，5位字母、数字
         */
        public static final String CAPTCHA_CODE_REGEX = "^[0-9a-zA-Z]{5}$";

        /**
         * 雪花算法生成的id正则，19位的数字
         */
        public static final String SNOW_ID_REGEX = "^\\d{19}$";
        /**
         * 姓名正则，2-24个字，只允许字母、数字、汉字
         */
        public static final String NAME_REGEX = "^[\\u4E00-\\u9FA5A-Za-z0-9]{2,24}$";

        /**
         * 个性签名正则，0-64个字符，不允许\n
         */
        public static final String SIGNATURE_REGEX = ".{0,64}$";

        /**
         * 性别正则，男 或 女
         */
        public static final String SEX_REGEX = "^[\\u7537\\u5973]$";

        /**
         * 图片链接正则，只允许以 .png .jpg .jpeg 结尾
         */
        public static final String IMAGE_REGEX = "^https://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?\\.(png|jpg|jpeg)$";
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

    /**
     * 是否是无效uuid格式
     *
     * @param uuid 要校验的uuid
     * @return true:符合，false：不符合
     */
    public static boolean isUUIDInvalid(String uuid) {
        return mismatch(uuid, RegexPatterns.UUID_REGEX);
    }

    /**
     * 是否是无效CaptchaCode字符格式
     *
     * @param code 要校验的code
     * @return true:符合，false：不符合
     */
    public static boolean isCaptchaCodeInvalid(String code) {
        return mismatch(code, RegexPatterns.CAPTCHA_CODE_REGEX);
    }

    /**
     * 是否是无效雪花算法生成的id字符格式
     *
     * @param id 要校验的id
     * @return true:符合，false：不符合
     */
    public static boolean isSnowIdInvalid(Long id) {
        return mismatch(id.toString(), RegexPatterns.SNOW_ID_REGEX);
    }

    /**
     * 是否是无效名字字符格式
     *
     * @param name 要校验的id
     * @return true:符合，false：不符合
     */
    public static boolean isNameInvalid(String name) {
        return mismatch(name, RegexPatterns.NAME_REGEX);
    }

    /**
     * 是否是无效个性签名字符格式
     *
     * @param signature 要校验的signature
     * @return true:符合，false：不符合
     */
    public static boolean isSignatureInvalid(String signature) {
        return mismatch(signature, RegexPatterns.SIGNATURE_REGEX);
    }
    /**
     * 是否是无效个性签名字符格式
     *
     * @param sex 要校验的sex
     * @return true:符合，false：不符合
     */
    public static boolean isSexInvalid(String sex) {
        return mismatch(sex, RegexPatterns.SEX_REGEX);
    }

    /**
     * 是否是无效图片链接字符格式
     *
     * @param imageUrl imageUrl
     * @return true:符合，false：不符合
     */
    public static boolean isImageInvalid(String imageUrl) {
        return mismatch(imageUrl, RegexPatterns.IMAGE_REGEX);
    }

    public static void main(String[] args) {
        System.out.println(isSignatureInvalid("wo  "));
    }

}