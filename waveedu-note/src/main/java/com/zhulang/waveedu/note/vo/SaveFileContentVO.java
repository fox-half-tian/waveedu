package com.zhulang.waveedu.note.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 狐狸半面添
 * @create 2023-03-05 19:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveFileContentVO {
    /**
     * 文件id
     */
    @NotNull(message = "文件id不允许为空")
    @Min(value = 1,message = "文件id格式错误")
    private Integer fileId;
    /**
     * 文件内容
     */
    @NotNull
    private String content;
}
