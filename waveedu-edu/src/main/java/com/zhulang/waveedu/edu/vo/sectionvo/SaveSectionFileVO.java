package com.zhulang.waveedu.edu.vo.sectionvo;

import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @author 狐狸半面添
 * @create 2023-02-16 16:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveSectionFileVO {
    /**
     * 小节id
     */
    @NotNull(message = "小节id不允许为空")
    @Min(value = 1, message = "小节不存在")
    private Integer sectionId;

    /**
     * 文件名，不可超过255长度
     */
    @NotBlank(message = "文件名不能为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.FILE_NAME_REGEX, message = "文件名不能超过255长度")
    private String fileName;

    /**
     * 上传者id
     */
    @Null(message = "非法操作")
    private Long userId;

    /**
     * 文件信息（加密处理）
     */
    @NotBlank(message = "文件信息不能为空")
    private String fileInfo;
}
