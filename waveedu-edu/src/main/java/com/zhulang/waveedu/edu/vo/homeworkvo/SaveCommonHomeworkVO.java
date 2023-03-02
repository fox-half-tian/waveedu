package com.zhulang.waveedu.edu.vo.homeworkvo;

import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

/**
 * 添加作业的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-28 0:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveCommonHomeworkVO {
    /**
     * 作业类型，0表示探究题，1表示其它类型的题目
     */
    @NotNull(message = "作业类型不允许为空")
    @Range(min = 0,max = 1,message = "作业类型格式错误")
    private Integer type;

    /**
     * 作业的标题，不超过64字
     */
    @NotBlank(message = "作业标题不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.LESSON_CLASS_HOMEWORK_TITLE_REGEX,message = "作业标题不超过64字")
    private String title;

    /**
     * 作业所属课程班级
     */
    @SnowIdValidate(message = "班级id格式错误")
    private Long lessonClassId;
}
