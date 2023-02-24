package com.zhulang.waveedu.edu.vo.lessonfilevo;

import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改文件名的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-14 19:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyFileNameVO {
    /**
     * 课程文件id
     */
    @SnowIdValidate(message = "文件id无效")
    private Long fileId;

    /**
     * 文件名，不可超过255长度
     */
    @NotBlank(message = "文件名不能为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.FILE_NAME_REGEX,message = "文件名不能超过255长度")
    private String fileName;
}
