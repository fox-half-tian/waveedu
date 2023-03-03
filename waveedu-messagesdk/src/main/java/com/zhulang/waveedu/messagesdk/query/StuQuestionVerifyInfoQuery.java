package com.zhulang.waveedu.messagesdk.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-04 3:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuQuestionVerifyInfoQuery {
    /**
     * 回答id
     */
    private Integer answerId;
    /**
     * 学生答案
     */
    private String stuAnswer;
    /**
     * 问题类型
     */
    private Integer questionType;
    /**
     * 参考答案
     */
    private String suggestAnswer;
    /**
     * 满分
     */
    private Integer fullScore;
}
