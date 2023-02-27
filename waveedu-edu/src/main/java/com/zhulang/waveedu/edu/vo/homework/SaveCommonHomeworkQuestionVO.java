package com.zhulang.waveedu.edu.vo.homework;

import com.baomidou.mybatisplus.annotation.*;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.edu.constant.ParamConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @since 2023-02-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveCommonHomeworkQuestionVO {

    /**
     * 作业id
     */
    @NotNull(message = "作业id不允许为空")
    @Min(value = 1, message = "作业id格式错误")
    private Integer commonHomeworkId;

    /**
     * 题目类型：0-单选,1-多选,2-填空，3-判断,4-探究
     */
    @NotNull(message = "题目类型不允许为空")
    @Range(min = 0, max = 4, message = "题目类型格式错误")
    private Integer type;

    /**
     * 问题描述
     */
    @NotBlank
    @Length(min = 1, max = ParamConstants.COMMON_HOMEWORK_MAX_PROBLEM_DESC_LENGTH, message = "问题描述不超过1000字")
    private String problemDesc;

    /**
     * 答案
     */
    @Length(min = 0, max = ParamConstants.COMMON_HOMEWORK_MAX_ANSWER_LENGTH, message = "答案不超过1000字")
    private String answer;

    /**
     * 解析
     */
    @Length(min = 0, max = ParamConstants.COMMON_HOMEWORK_MAX_ANALYSIS_LENGTH, message = "解析不超过1000字")
    private String analysis;

    /**
     * 分值
     */
    @NotNull(message = "分值不允许为空")
    @Min(value = 0, message = "分值最少为0分")
    private Integer score;
}
