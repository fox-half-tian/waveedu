package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程班级的上课时间表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
@TableName("edu_lesson_class_attend")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonClassAttend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id（雪花算法）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 班级id
     */
    private Long lessonClassId;

    /**
     * 星期：可选1~7
     */
    private Integer week;

    /**
     * 每天的哪个时间，可选1-5
     */
    private Integer time;

    /**
     * 课程名
     */
    private String lessonName;

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
