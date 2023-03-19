package com.zhulang.waveedu.share.test;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-03-19 11:49
 */
public class CommonTest {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("高数");
        list.add("政治");
        list.add("英语");
        String s = JSON.toJSONString(list);
        List<String> object = JSON.parseObject(s, List.class);
        for (String s1 : object) {
            System.out.println(s1);
        }
    }
}
