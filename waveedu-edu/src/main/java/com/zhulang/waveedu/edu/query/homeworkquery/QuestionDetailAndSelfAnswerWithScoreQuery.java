package com.zhulang.waveedu.edu.query.homeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-03 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDetailAndSelfAnswerWithScoreQuery {
    /**
     * 问题id
     */
    private Integer id;
    /**
     * 问题类型
     */
    private Integer type;
    /**
     * 问题描述
     */
    private String problemDesc;
    /**
     * 参考答案
     */
    private String suggestAnswer;
    /**
     * 解析
     */
    private String analysis;
    /**
     * 本题满分
     */
    private Integer fullScore;
    /**
     * 学生答案
     */
    private String stuAnswer;
    /**
     * 学生分数
     */
    private Integer stuScore;
}
