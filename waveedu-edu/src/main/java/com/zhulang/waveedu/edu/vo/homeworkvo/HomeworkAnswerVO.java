package com.zhulang.waveedu.edu.vo.homeworkvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-04 0:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkAnswerVO {
    /**
     * 问题id
     */
    private Integer questionId;
    /**
     * 答案
     */
    private String answer;
}
