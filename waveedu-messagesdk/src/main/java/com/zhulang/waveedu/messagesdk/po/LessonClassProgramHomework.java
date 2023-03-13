package com.zhulang.waveedu.messagesdk.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 编程作业表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-13
 */
@TableName("edu_lesson_class_program_homework")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonClassProgramHomework implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id（自增）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作业的标题
     */
    private String title;

    /**
     * 作业所属课程班级
     */
    private Long classId;

    /**
     * 作业的创建者
     */
    private Long creatorId;

    /**
     * 题目数量
     */
    private Integer num;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 截止时间
     */
    private LocalDateTime endTime;

    /**
     * 是否发布，0-未发布，1-已发布，2-发布中
     */
    private Integer isPublish;

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
