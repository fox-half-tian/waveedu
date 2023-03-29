package com.zhulang.waveedu.edu.vo.lessonvo;

import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 创建课程的VO
 *
 * @author 狐狸半面添
 * @create 2023-02-03 16:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveLessonVO {
    /**
     * 课程名，最多24长度，不允许为空
     */
    @NotBlank(message = "课程名不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.LESSON_NAME_REGEX, message = "只允许非空并且24字内的课程名")
    private String name;

    /**
     * 课程介绍，最多512长度
     */
    @Length(min=0,max = 512, message = "只允许512字内的介绍")
    private String introduce;

    /**
     * 课程封面-链接
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.IMAGE_REGEX, message = "错误的图片链接")
    private String cover;
}
