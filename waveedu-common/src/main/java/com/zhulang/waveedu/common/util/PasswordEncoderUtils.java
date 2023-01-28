package com.zhulang.waveedu.common.util;

import cn.hutool.core.util.RandomUtil;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author 狐狸半面添
 * @create 2023-01-18 19:55
 */
public class PasswordEncoderUtils {

    /**
     * 加密
     *
     * @param password 真实密码
     * @return 加密后的密码
     */
    public static String encode(String password) {
        // 生成盐
        String salt = RandomUtil.randomString(20);
        // 加密
        return encode(password,salt);
    }
    private static String encode(String password, String salt) {
        // 加密
        return salt + "@" + DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 密码核实
     *
     * @param encodedPassword 加密后的密码
     * @param rawPassword 用户输入的密码
     * @return true:验证成功  false:验证失败
     */
    public static Boolean matches(String encodedPassword, String rawPassword) {
        if (encodedPassword == null || rawPassword == null || !encodedPassword.contains("@")) {
            return false;
        }
        String[] arr = encodedPassword.split("@");
        // 获取盐
        String salt = arr[0];
        // 比较
        return encodedPassword.equals(encode(rawPassword, salt));
    }

    public static void main(String[] args) {
        String encode = encode("12345678");
        System.out.println(encode);
        System.out.println(encode.length());
    }
}
