package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class LessonClassCommonHomework implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id（自增）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作业类型，0表示探究题，1其它表示类型的题目
     */
    private Integer type;

    /**
     * 作业的标题
     */
    private String title;

    /**
     * 作业所属课程班级
     */
    private Long lessonClassId;

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
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Long getLessonClassId() {
        return lessonClassId;
    }

    public void setLessonClassId(Long lessonClassId) {
        this.lessonClassId = lessonClassId;
    }
    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }
    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
        return "LessonClassCommonHomework{" +
            "id=" + id +
            ", type=" + type +
            ", title=" + title +
            ", lessonClassId=" + lessonClassId +
            ", difficulty=" + difficulty +
            ", totalScore=" + totalScore +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
