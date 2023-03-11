package com.zhulang.waveedu.program.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 编程问题题库表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-11
 */
@TableName("program_problem_bank")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemBank implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 作者类型：0-普通用户，1-管理员
     */
    private Integer authorType;

    /**
     * 作者id
     */
    private Long authorId;

    /**
     * 是否公开
     */
    private Integer isPublic;

    /**
     * 单位ms
     */
    private Integer timeLimit;

    /**
     * 单位kb
     */
    private Integer memoryLimit;

    /**
     * 单位mb
     */
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
     * 题目难度：0简单，1中等，2困难
     */
    private Integer difficulty;

    /**
     * 提示
     */
    private String hint;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
