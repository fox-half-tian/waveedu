package com.zhulang.waveedu.edu.vo;

import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 保存章节时的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-15 18:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveChapterVO {
    /**
     * 课程id
     */
    @SnowIdValidate(message = "课程id格式错误")
    private Long lessonId;
    /**
     * 章节名，不可超过24长度
     */
    @NotBlank(message = "章节名不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.LESSON_CHAPTER_NAME_REGEX,message = "章节名不超过24字")
    private String name;
}
