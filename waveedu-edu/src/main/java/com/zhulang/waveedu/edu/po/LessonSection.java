package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程章节的小节表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-15
 */
@TableName("edu_lesson_section")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonSection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 章节id
     */
    private Integer chapterId;

    /**
     * 小节号
     */
    private Integer orderBy;

    /**
     * 小节名，不可超过24长度
     */
    private String name;

    /**
     * 创建者id
     */
    private Long creatorId;

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
