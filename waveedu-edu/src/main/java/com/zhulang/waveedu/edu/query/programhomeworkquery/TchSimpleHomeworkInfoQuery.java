package com.zhulang.waveedu.edu.query.programhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-13 18:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TchSimpleHomeworkInfoQuery {
    /**
     * 作业id
     */
    private Integer id;
    /**
     * 作业标题
     */
    private String title;
    /**
     * 题目数量
     */
    private Integer num;
    /**
     * 作业状态
     */
    private Integer status;
    /**
     * 截止时间
     */
    private LocalDateTime endTime;
}
