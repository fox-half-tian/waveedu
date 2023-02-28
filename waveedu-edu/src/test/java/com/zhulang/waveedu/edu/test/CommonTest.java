package com.zhulang.waveedu.edu.test;

import com.alibaba.fastjson.JSON;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.edu.po.LessonFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-02-11 20:40
 */
public class CommonTest {
    public static void main(String[] args) {
        test03();
    }

    public static void test03(){
        List<String> info = new ArrayList<>();
        info.add("狐狸半面添");
        info.add("唐雨浪");
        String s = JSON.toJSONString(info);
        System.out.println(s);
        System.out.println(JSON.toJSONString(s));
        ArrayList arrayList = JSON.parseObject(s, ArrayList.class);
        System.out.println(arrayList.get(0)+"    "+arrayList.get(1));
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
