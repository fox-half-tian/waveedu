package com.zhulang.waveedu.note.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 狐狸半面添
 * @create 2023-03-06 23:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleFileInfoVO {
    /**
     * 文件id
     */
    @NotNull(message = "文件id不允许为空")
    @Min(value = 1, message = "文件Id格式错误")
    private Integer fileId;
    /**
     * 文件名
     */
    @NotBlank(message = "文件名不允许为空")
    @Length(min = 1, max = 64, message = "文件名最多64字")
    private String fileName;
}
