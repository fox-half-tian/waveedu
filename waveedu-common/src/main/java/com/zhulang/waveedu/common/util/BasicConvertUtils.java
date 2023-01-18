package com.zhulang.waveedu.common.util;

/**
 * @author 狐狸半面添
 * @create 2023-01-18 20:34
 */
public class BasicConvertUtils {
    public static String[] strSplitToArr(String content, String splitStr){
        return content.split(splitStr);
    }


    public static void main(String[] args) {
        String[] strings = strSplitToArr("1,2,3,4", ",");
        for (String string : strings) {
            System.out.println(string);
        }
    }
}
