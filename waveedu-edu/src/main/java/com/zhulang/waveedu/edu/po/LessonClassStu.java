package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程班级与学生的对应关系表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-17
 */
@TableName("edu_lesson_class_stu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonClassStu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程id
     */
    private Long lessonId;

    /**
     * 课程班级的id
     */
    private Long lessonClassId;

    /**
     * 学生的id
     */
    private Long stuId;

    /**
     * 创建时间（加入时间）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
