package com.zhulang.waveedu.program.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 狐狸半面添
 * @create 2023-03-11 22:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyProblemVO {
    /**
     * 问题id
     */
    @NotNull(message = "问题id不允许为空")
    @Min(value = 1000,message = "问题id格式错误")
    private Integer id;

    /**
     * 标题
     */
    @Length(min = 1,max = 255,message = "标题字数1-255之间")
    private String title;

    /**
     * 是否公开
     */
    @Range(min = 0,max = 1,message = "公开设置格式错误")
    private Integer isPublic;

    /**
     * 时间限制(ms)，默认为c/c++限制,其它语言为2倍
     */
    @Min(value = 0,message = "运行时间最小值设置为0ms")
    private Integer timeLimit;

    /**
     * 空间限制(mb)，默认为c/c++限制,其它语言为2倍
     */
    @Min(value = 0,message = "内存限制最小值设置为0mb")
    private Integer memoryLimit;

    /**
     * 栈限制(mb)，默认为128
     */
    @Min(value = 0,message = "栈限制最小值设置为0mb")
    private Integer stackLimit;

    /**
     * 题目描述
     */
    private String description;

    /**
     * 输入
     */
    private String input;

    /**
     * 输出
     */
    private String output;

    /**
     * 题面样例
     */
    private String examples;

    /**
     * 难度
     */
    @Range(min = 0,max = 2,message = "难度设置格式错误")
    private Integer difficulty;

    /**
     * 提示
     */
    private String hint;


}
