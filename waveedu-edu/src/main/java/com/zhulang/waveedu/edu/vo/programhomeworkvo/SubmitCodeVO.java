package com.zhulang.waveedu.edu.vo.programhomeworkvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 狐狸半面添
 * @create 2023-03-14 20:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitCodeVO {
    /**
     * 问题id
     */
    @NotNull(message = "问题id不允许为空")
    @Min(value = 1000,message = "无效问题id")
    private Integer problemId;
    /**
     * 使用的语言
     */
    @NotBlank(message = "请输入使用的语言")
    private String language;
    /**
     * 代码
     */
    @NotBlank(message = "无效代码")
    private String code;

}
