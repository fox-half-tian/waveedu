package com.zhulang.waveedu.edu.query.homeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-04 2:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkIdAndTypeAndEndTimeQuery {
    /**
     * 问题id
     */
    private Integer homeworkId;
    /**
     * 问题类型
     */
    private Integer homeworkType;
    /**
     * 截止时间
     */
    private LocalDateTime endTime;
}
