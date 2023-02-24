package com.zhulang.waveedu.edu.vo.lessonvo;

import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 用于修改基本课程信息的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-12 1:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyLessonBasicInfoVO {
    /**
     * 主键id
     */
    @SnowIdValidate(message = "无效课程id")
    private Long id;
    /**
     * 课程名，最多24长度
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.LESSON_NAME_REGEX, message = "只允许非空并且24字内的课程名")
    private String name;

    /**
     * 课程介绍，最多512长度
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.LESSON_INTRO__REGEX, message = "只允许512字内的介绍")
    private String introduce;

    /**
     * 课程封面-链接
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.IMAGE_REGEX, message = "错误的图片链接")
    private String cover;
}
