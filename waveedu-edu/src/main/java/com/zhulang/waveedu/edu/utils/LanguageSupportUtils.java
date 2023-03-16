package com.zhulang.waveedu.edu.utils;

import java.util.HashSet;

/**
 * 支持的判题语言
 *
 * @author 狐狸半面添
 * @create 2023-03-15 0:56
 */
public class LanguageSupportUtils {
    public static final HashSet<String> LANGUAGE_LIB = new HashSet<>();

    static {
        LANGUAGE_LIB.add("Java");
        LANGUAGE_LIB.add("C");
        LANGUAGE_LIB.add("C++");
        LANGUAGE_LIB.add("Golang");
        LANGUAGE_LIB.add("Python3");
    }

    /**
     * 判断判题机是否支持该语言
     *
     * @param language 语言
     * @return true-支持，false-不支持
     */
    public static boolean isSupport(String language){
        return LANGUAGE_LIB.contains(language);
    }

}
