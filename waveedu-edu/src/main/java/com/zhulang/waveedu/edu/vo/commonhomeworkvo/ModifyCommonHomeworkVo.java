package com.zhulang.waveedu.edu.vo.commonhomeworkvo;

import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-01 23:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyCommonHomeworkVo {
    /**
     * 作业的id
     */
    @NotNull(message = "作业Id不允许为空")
    @Min(value = 1,message = "作业id格式错误")
    private Integer id;

    /**
     * 作业的标题，不超过64字
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.LESSON_CLASS_HOMEWORK_TITLE_REGEX,message = "作业标题不超过64字")
    private String title;

    /**
     * 难度：0表示简单，1表示中等，2表示困难
     */
    @Range(min = 0,max = 2,message = "难度选择格式错误")
    private Integer difficulty;

    /**
     * 截止时间
     */
    @Future(message = "截止时间必须是一个未来的时间")
    private LocalDateTime endTime;

    /**
     * 完成作业后是否开启解析，0表示不开启，1表示开启，默认0
     */
    @Range(min = 0,max = 1,message = "解析策略参数格式错误")
    private Integer isCompleteAfterExplain;
    /**
     * 时间截止后是否开启解析，0表示不开启，1表示开启，默认1
     */
    @Range(min = 0,max = 1,message = "解析策略参数格式错误")
    private Integer isEndAfterExplain;

}
