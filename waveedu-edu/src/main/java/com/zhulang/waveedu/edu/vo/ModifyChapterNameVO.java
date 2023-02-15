package com.zhulang.waveedu.edu.vo;

import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 修改章节名的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-16 1:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyChapterNameVO {
    /**
     * 章节id
     */
    @NotNull(message = "章节id不允许为空")
    @Min(value = 1, message = "章节id格式错误")
    private Integer chapterId;
    /**
     * 章节name
     */
    @NotBlank(message = "章节名不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.LESSON_CHAPTER_NAME_REGEX, message = "章节名不超过24字")
    private String chapterName;
}
