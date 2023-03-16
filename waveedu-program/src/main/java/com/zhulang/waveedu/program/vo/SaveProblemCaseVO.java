package com.zhulang.waveedu.program.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 狐狸半面添
 * @create 2023-03-12 0:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveProblemCaseVO {
    /**
     * 题目id
     */
    @NotNull(message = "问题id不允许为空")
    @Min(value = 1000,message = "问题id格式错误")
    private Integer problemId;
    /**
     * 测试样例的输入
     */
    private String input;
    /**
     * 测试样例的输出
     */
    private String output;
}
