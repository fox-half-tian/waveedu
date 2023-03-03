package com.zhulang.waveedu.messagesdk.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 普通作业表的学生回答表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-04
 */
@TableName("edu_common_homework_stu_answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonHomeworkStuAnswer implements Serializable {

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
    private Integer questionId;

    /**
     * 回答
     */
    private String answer;

    /**
     * 本题获得的分数
     */
    private Integer score;

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
