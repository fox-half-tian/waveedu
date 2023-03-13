package com.zhulang.waveedu.edu.query.commonhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-04 18:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkAnyStatusStuInfoQuery {
    /**
     * 学生姓名
     */
    private String stuName;
    /**
     * 学生id
     */
    private Long stuId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 学生分数
     */
    private Integer stuScore;
    /**
     * 提交时间
     */
    private LocalDateTime commitTime;
}
