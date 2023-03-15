package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 编程作业的学生正确回答表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-15
 */
@TableName("edu_program_homework_stu_answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramHomeworkStuAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id(自增)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作业id
     */
    private Integer homeworkId;

    /**
     * 学生id
     */
    private Long stuId;

    /**
     * 问题的id
     */
    private Integer problemId;

    /**
     * 运行时间(ms)
     */
    private Integer time;

    /**
     * 内存大小(kb)
     */
    private Integer memory;

    /**
     * 使用语言
     */
    private String language;

    /**
     * 代码
     */
    private String code;

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
