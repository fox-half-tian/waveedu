package com.waveedu.edu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
public class LessonTch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
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
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        return "LessonTch{" +
            "id=" + id +
            ", lessonId=" + lessonId +
            ", userId=" + userId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
