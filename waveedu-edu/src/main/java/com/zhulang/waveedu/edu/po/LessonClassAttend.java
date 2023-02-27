package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.TableName;
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
public class LessonClassAttend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id（雪花算法）
     */
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
    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }
    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
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
        return "LessonClassAttend{" +
            "id=" + id +
            ", lessonClassId=" + lessonClassId +
            ", week=" + week +
            ", time=" + time +
            ", lessonName=" + lessonName +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
