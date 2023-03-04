package com.zhulang.waveedu.edu.query.homeworkquery;

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
public class HomeworkNoCheckStuInfoQuery {
    /**
     * 学生姓名
     */
    private String stuName;
    /**
     * 学生id
     */
    private Long stuId;
    /**
     * 提交时间
     */
    private LocalDateTime commitTime;
}
