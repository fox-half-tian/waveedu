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
 * 修改小节名的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-16 0:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifySectionNameVO {
    /**
     * 小节id
     */
    @NotNull(message = "小节id不允许为空")
    @Min(value = 1, message = "小节id格式错误")
    private Integer sectionId;
    /**
     * 小节name
     */
    @NotBlank(message = "小节名不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.LESSON_SECTION_NAME_REGEX, message = "小节名不超过24字")
    private String sectionName;
}
