package com.zhulang.waveedu.edu.query.homeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-04 21:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkNoCheckTaskInfoQuery {
    /**
     * 学生id
     */
    private Long stuId;
    /**
     * 作业id
     */
    private Integer homeworkId;
    /**
     * 分数表id
     */
    private Integer scoreId;
    /**
     * 作业标题
     */
    private String homeworkTitle;
    /**
     * 学生姓名
     */
    private String stuName;
    /**
     * 提交时间
     */
    private LocalDateTime commitTime;
}
