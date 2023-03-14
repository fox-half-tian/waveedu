package com.zhulang.waveedu.edu.utils;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 支持的判题语言
 *
 * @author 狐狸半面添
 * @create 2023-03-15 0:56
 */
public class SupportLanguageUtils {
    public static final HashSet<String> LANGUAGE_LIB = new HashSet<>();

    static {
        LANGUAGE_LIB.add("Java");
        LANGUAGE_LIB.add("C");
        LANGUAGE_LIB.add("C++");
        LANGUAGE_LIB.add("Golang");
        LANGUAGE_LIB.add("Python3");
    }
}
