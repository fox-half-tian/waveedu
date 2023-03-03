package com.zhulang.waveedu.edu.query.homeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-03 10:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuHomeworkDetailInfoQuery {
    /**
     * 类型
     */
    private Integer type;
    /**
     * 标题
     */
    private String title;
    /**
     * 难度
     */
    private Integer difficulty;
    /**
     * 总分
     */
    private Integer totalScore;
    /**
     * 学生分数
     */
    private Integer stuScore;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 教师评价
     */
    private String comment;
}
