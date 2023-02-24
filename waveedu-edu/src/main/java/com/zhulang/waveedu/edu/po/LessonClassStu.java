package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getLessonClassId() {
        return lessonClassId;
    }

    public void setLessonClassId(Long lessonClassId) {
        this.lessonClassId = lessonClassId;
    }
    public Long getStuId() {
        return stuId;
    }

    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "LessonClassStu{" +
            "id=" + id +
            ", lessonClassId=" + lessonClassId +
            ", stuId=" + stuId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
