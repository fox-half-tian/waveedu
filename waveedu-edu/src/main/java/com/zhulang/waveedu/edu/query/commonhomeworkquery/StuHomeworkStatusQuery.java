package com.zhulang.waveedu.edu.query.commonhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-03 12:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuHomeworkStatusQuery {
    /**
     * 作业状态
     */
    private Integer status;
    /**
     * 是否开启完成作业后解析
     */
    private Integer isCompleteAfterExplain;
    /**
     * 是否开启时间截止后解析
     */
    private Integer isEndAfterExplain;
    /**
     * 截止时间
     */
    private LocalDateTime endTime;
}
