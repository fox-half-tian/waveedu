package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程与教学团队的对应表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-03
 */
@TableName("edu_lesson_tch")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonTch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 课程id
     */
    private Long lessonId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;


}
