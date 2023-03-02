package com.zhulang.waveedu.edu.query.homeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-02 21:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TchHomeworkQuestionDetailInfoQuery {
    /**
     * 主键id（自增）
     */
    private Integer id;

    /**
     * 题目类型：0-单选,1-多选,2-填空，3-判断,4-探究
     */
    private Integer type;

    /**
     * 问题描述
     */
    private String problemDesc;

    /**
     * 答案
     */
    private String answer;

    /**
     * 解析
     */
    private String analysis;

    /**
     * 分值
     */
    private Integer score;
}
