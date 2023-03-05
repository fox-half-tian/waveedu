package com.zhulang.waveedu.note.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 狐狸半面添
 * @create 2023-03-05 11:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveFileVO {
    /**
     * 父目录，最高级目录的parentId 是0
     */
    @NotNull(message = "父目录不允许为空")
    @Min(value = 0,message = "父级目录id格式错误")
    private Integer parentId;
    /**
     * 文件名
     */
    @NotBlank(message = "文件名不允许为空")
    @Length(min = 1,max = 64,message = "文件名最多64字")
    private String name;
    /**
     * 类型
     */
    @NotNull(message = "文件类型不允许为空")
    @Range(min = 0,max = 1,message = "类型格式错误")
    private Integer type;
}
