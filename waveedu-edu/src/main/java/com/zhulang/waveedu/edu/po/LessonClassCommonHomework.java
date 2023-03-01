package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程班级的普通作业表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
@TableName("edu_lesson_class_common_homework")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonClassCommonHomework implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id（自增）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作业类型，0表示探究题，1表示其它类型的题目
     */
    private Integer type;

    /**
     * 作业的标题，不超过64字
     */
    private String title;

    /**
     * 作业所属课程班级
     */
    private Long lessonClassId;

    /**
     * 作业创建者
     */
    private Long creatorId;

    /**
     * 难度：0表示简单，1表示中等，2表示困难
     */
    private Integer difficulty;

    /**
     * 总分
     */
    private Integer totalScore;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 截止时间
     */
    private LocalDateTime endTime;

    /**
     * 是否发布，0-未发布，1-发布
     */
    private Integer isPublish;

    /**
     * 完成作业后是否开启解析，0表示不开启，1表示开启，默认0
     */
    private Integer isCompleteAfterExplain;

    /**
     * 时间截止后是否开启解析，0表示不开启，1表示开启，默认1
     */
    private Integer isEndAfterExplain;

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
