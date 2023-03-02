package com.zhulang.waveedu.edu.query.classquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 班级时间安排的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-27 17:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassAttendDayTimeQuery {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 时间段
     */
    private Integer time;
    /**
     * 课程名
     */
    private String lessonName;

}
