package com.zhulang.waveedu.edu.test;

import com.alibaba.fastjson.JSON;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.edu.po.LessonFile;
import com.zhulang.waveedu.edu.vo.commonhomeworkvo.MarkHomeworkVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-02-11 20:40
 */
public class CommonTest {
    public static void main(String[] args) {
        test04();
    }

    public static void test04(){
        List<String> list = new ArrayList<>();
        for (String s : list) {
            System.out.println("s");
        }
    }

    public static void test03(){
        MarkHomeworkVO markHomeworkVO = new MarkHomeworkVO();
        ArrayList<MarkHomeworkVO.InnerMark> innerMarks = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            MarkHomeworkVO.InnerMark innerMark = new MarkHomeworkVO.InnerMark();
            innerMark.setScore(8);
            innerMark.setQuestionId(16);
            innerMarks.add(innerMark);
        }
        markHomeworkVO.setComment("哈哈哈");
        markHomeworkVO.setStuId(123456L);
        markHomeworkVO.setInnerMarkList(innerMarks);
        String info = JSON.toJSONString(markHomeworkVO);
        System.out.println(info);
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
