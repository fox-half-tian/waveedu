package com.zhulang.waveedu.edu.query.commonhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-03 1:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuHomeworkSimpleInfoQuery {
    /**
     * 作业id
     */
    private Integer id;
    /**
     * 作业标题
     */
    private String title;
    /**
     * 作业截止时间
     */
    private LocalDateTime endTime;
    /**
     * 学生对作业的状态
     */
    private Integer status;
}
