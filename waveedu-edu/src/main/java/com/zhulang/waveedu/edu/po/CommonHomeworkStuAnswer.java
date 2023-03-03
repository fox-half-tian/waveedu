package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 普通作业表的学生回答表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-04
 */
@TableName("edu_common_homework_stu_answer")
public class CommonHomeworkStuAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id(自增)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作业id
     */
    private Integer commonHomeworkId;

    /**
     * 学生id
     */
    private Long stuId;

    /**
     * 问题的id
     */
    private Integer questionId;

    /**
     * 回答
     */
    private String answer;

    /**
     * 本题获得的分数
     */
    private Integer score;

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
    public Integer getCommonHomeworkId() {
        return commonHomeworkId;
    }

    public void setCommonHomeworkId(Integer commonHomeworkId) {
        this.commonHomeworkId = commonHomeworkId;
    }
    public Long getStuId() {
        return stuId;
    }

    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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
        return "CommonHomeworkStuAnswer{" +
            "id=" + id +
            ", commonHomeworkId=" + commonHomeworkId +
            ", stuId=" + stuId +
            ", questionId=" + questionId +
            ", answer=" + answer +
            ", score=" + score +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
