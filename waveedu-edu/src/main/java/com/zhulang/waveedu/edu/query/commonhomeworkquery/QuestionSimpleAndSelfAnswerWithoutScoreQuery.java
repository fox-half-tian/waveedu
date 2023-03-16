package com.zhulang.waveedu.edu.query.commonhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-03 19:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSimpleAndSelfAnswerWithoutScoreQuery {
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
     * 本题满分
     */
    private Integer fullScore;
    /**
     * 学生答案
     */
    private String stuAnswer;
}
