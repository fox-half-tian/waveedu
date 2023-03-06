package com.zhulang.waveedu.note.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 狐狸半面添
 * @create 2023-03-06 23:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyFileLocationVO {
    /**
     * 需要移动的文件或目录
     */
    @NotNull(message = "移动文件id不允许为空")
    @Min(value = 1, message = "文件Id格式错误")
    private Integer fromFileId;
    /**
     * 移动到的目录
     */
    @NotNull(message = "移动到的目录id不允许为空")
    @Min(value = 0, message = "文件Id格式错误")
    private Integer toDirId;
}
