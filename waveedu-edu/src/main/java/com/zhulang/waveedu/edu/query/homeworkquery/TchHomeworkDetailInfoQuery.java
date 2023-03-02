package com.zhulang.waveedu.edu.query.homeworkquery;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-02 21:59
 */
public class TchHomeworkDetailInfoQuery {
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
     * 提交人数
     */
    private Integer commitNum;

    /**
     * 班级学生总人数
     */
    private Integer classStuNum;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
