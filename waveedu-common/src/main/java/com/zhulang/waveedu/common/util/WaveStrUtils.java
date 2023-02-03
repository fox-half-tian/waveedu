package com.zhulang.waveedu.common.util;

import cn.hutool.core.util.RandomUtil;

/**
 * 对字符串进行处理的工具类
 *
 * @author 狐狸半面添
 * @create 2023-01-18 20:34
 */
public class WaveStrUtils {
    /**
     * 将字符串通过分割方式转为数组
     *
     * @param content 字符串
     * @param splitStr 分割标识
     * @return 数组
     */
    public static String[] strSplitToArr(String content, String splitStr){
        if (content==null||splitStr==null){
            return null;
        }
        return content.split(splitStr);
    }

    /**
     * 去除字符串的所有空白字符
     *
     * @param content 字符串
     * @return 无空白字符的字符串
     */
    public static String removeBlank(String content){
        if (content==null){
            return null;
        }
        return content.replaceAll("\\s+","");
    }

    public static void main(String[] args) {
        System.out.println(RandomUtil.randomString(6));
    }
}
