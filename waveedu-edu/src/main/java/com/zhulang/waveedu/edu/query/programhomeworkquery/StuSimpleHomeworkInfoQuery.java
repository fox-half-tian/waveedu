package com.zhulang.waveedu.edu.query.programhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-15 17:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuSimpleHomeworkInfoQuery {
    /**
     * 问题id
     */
    private Integer problemId;
    /**
     * 标题
     */
    private String title;
    /**
     * 问题数量
     */
    private Integer problemNum;
    /**
     * 完成数量
     */
    private Integer completeNum;
    /**
     * 截止时间
     */
    private LocalDateTime endTime;
    /**
     * 状态
     */
    private Integer status;
}
