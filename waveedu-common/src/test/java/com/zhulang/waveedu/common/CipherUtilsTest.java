package com.zhulang.waveedu.common;

import com.zhulang.waveedu.common.util.CipherUtils;

/**
 * @author 狐狸半面添
 * @create 2023-02-17 14:28
 */
public class CipherUtilsTest {
    public static void main(String[] args) {
        String encrypt = CipherUtils.encrypt("hello");
        System.out.println(encrypt);
        System.out.println(CipherUtils.decrypt("h0mS2x4jQ06uktBVc4EaQ=="));
        System.out.println(CipherUtils.decrypt("rh0mS2x4jQ06uktBVc4EaQ="));
        System.out.println(CipherUtils.decrypt("rh0mS2x4jQ06uktBVc4EaQ"));
    }
}
