package com.zhulang.waveedu.edu.vo;

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
 * 保存章节时的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-15 23:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveSectionVO {
    /**
     * 章节id
     */
    @NotNull(message = "章节id不允许为空")
    @Min(value = 1, message = "章节id格式错误")
    private Integer chapterId;
    /**
     * 小节名，不可超过24长度
     */
    @NotBlank(message = "小节名不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.LESSON_SECTION_NAME_REGEX, message = "小节名不超过24字")
    private String name;
}
