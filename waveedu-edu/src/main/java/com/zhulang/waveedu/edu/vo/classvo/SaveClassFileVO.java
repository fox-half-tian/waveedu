package com.zhulang.waveedu.edu.vo.classvo;

import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

/**
 * 保存课程的班级资料的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-11 20:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveClassFileVO {
    /**
     * 班级id
     */
    @SnowIdValidate(message = "班级id无效")
    private Long lessonClassId;

    /**
     * 文件名，不可超过255长度
     */
    @NotBlank(message = "文件名不能为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.FILE_NAME_REGEX,message = "文件名不能超过255长度")
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
