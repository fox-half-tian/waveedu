package com.zhulang.waveedu.edu.vo.classvo;

import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 保存课程的视图类
 *
 * @author 狐狸半面添
 * @create 2023-02-24 20:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveClassVO {
    /**
     * 班级名，不可超过24长度
     */
    @NotBlank(message = "班级名不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.LESSON_CLASS_NAME_REGEX,message = "班级名不超过24字")
    private String name;

    /**
     * 班级封面（有默认值）
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.IMAGE_REGEX, message = "错误的图片链接")
    private String cover;

    /**
     * 所属课程id
     */
    @NotNull(message = "课程id不允许为空")
    @SnowIdValidate(message = "课程id格式错误")
    private Long lessonId;
}
