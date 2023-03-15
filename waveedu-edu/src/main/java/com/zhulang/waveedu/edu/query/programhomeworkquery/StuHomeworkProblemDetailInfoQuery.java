package com.zhulang.waveedu.edu.query.programhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-15 19:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuHomeworkProblemDetailInfoQuery {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 单位ms
     */
    private Integer timeLimit;

    /**
     * 单位mb
     */
    private Integer memoryLimit;

    /**
     * 单位mb
     */
    private Integer stackLimit;

    /**
     * 题目描述
     */
    private String description;

    /**
     * 输入
     */
    private String input;

    /**
     * 输出
     */
    private String output;

    /**
     * 题面样例
     */
    private String examples;

    /**
     * 题目难度：0简单，1中等，2困难
     */
    private Integer difficulty;

    /**
     * 提示
     */
    private String hint;

    /**
     * 使用的语言
     */
    private String language;

    /**
     * 代码
     */
    private String code;

    /**
     * 状态
     */
    private Integer status;
}
