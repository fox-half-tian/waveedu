package com.zhulang.waveedu.edu.test;

import com.alibaba.fastjson.JSON;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.edu.po.LessonFile;

import java.util.HashMap;

/**
 * @author 狐狸半面添
 * @create 2023-02-11 20:40
 */
public class CommonTest {
    public static void main(String[] args) {
        test02();
    }
    public static void test02(){
        Long a=1623220363309776897L;
        Long b=1623220363309776897L;
        System.err.println("a==b的结果是"+(a==b));
        System.err.println("a.longValue()==b.longValue()的结果是"+(a.longValue()==b.longValue()));
    }

    public static void test01(){
        String str = "ydhnPjgKLfApIlK9Wvvi2fR77raBlSsrOcWCe0AeFXWXn2Ofnep3TLrsT13LWuha9ylmAlJrMVWvu-IY-RC64LouMkALDGVCmRgwEdiORnz00bwFknbGRJ7bDgyTFb-xshPARLxmbKGUYn63nWQizo-aA3_UgHYYlOpAWwSZMCgi-SoSKJiT71PFvhTNIFwqtKYavy8QsSZV0gab7elBzi3cet36GPwe93YtHUkUCGbveOlG3ogO6UKn90fKOIWA";
        HashMap hashMap = JSON.parseObject(CipherUtils.decrypt(str), HashMap.class);
        System.out.println(hashMap);
        LessonFile lessonFile = JSON.parseObject(CipherUtils.decrypt(str), LessonFile.class);
        System.out.println(lessonFile);
    }
}
