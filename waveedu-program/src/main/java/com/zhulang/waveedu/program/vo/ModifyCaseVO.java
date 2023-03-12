package com.zhulang.waveedu.program.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 狐狸半面添
 * @create 2023-03-12 2:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyCaseVO {
    /**
     * 案例id
     */
    @NotNull(message = "案例id不允许为空")
    @Min(value = 1000,message = "案例id格式错误")
    private Integer id;
    /**
     * 输入
     */
    @NotNull(message = "输入不允许为空")
    private String  input;
    /**
     * 输出
     */
    @NotNull(message = "输出不允许为空")
    private String  output;
}