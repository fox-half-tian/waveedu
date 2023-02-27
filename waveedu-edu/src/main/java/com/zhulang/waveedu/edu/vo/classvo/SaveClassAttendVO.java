package com.zhulang.waveedu.edu.vo.classvo;

import com.baomidou.mybatisplus.annotation.*;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 保存班级排班的封装类
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveClassAttendVO{

    /**
     * 班级id
     */
    @SnowIdValidate(message = "班级id格式错误")
    private Long lessonClassId;

    /**
     * 星期：可选1~7
     */
    @NotNull(message = "需要选择上课星期")
    @Range(min = 1,max = 7,message = "上课星期格式错误")
    private Integer week;

    /**
     * 每天的哪个时间，可选1-5
     */
    @NotNull(message = "需要选择上课时间")
    @Range(min = 1,max = 5,message = "上课时间格式错误")
    private Integer time;

    /**
     * 课程名
     */
    @NotBlank(message = "课程名不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.LESSON_NAME_REGEX,message = "课程名不允许超过24字")
    private String lessonName;
}
