package com.zhulang.waveedu.edu.query.programhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-16 18:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkStuCompleteInfoQuery {
    /**
     * 学生id
     */
    private Long stuId;
    /**
     * 学生姓名
     */
    private String stuName;
    /**
     * 学生头像
     */
    private String stuIcon;
    /**
     * 完成数量
     */
    private Integer completeNum;
    /**
     * 全部完成的时间
     */
    private LocalDateTime allCompleteTime;
    /**
     * 状态：0-已完成，1-进行中，2-已截止
     */
    private Integer status;
}
