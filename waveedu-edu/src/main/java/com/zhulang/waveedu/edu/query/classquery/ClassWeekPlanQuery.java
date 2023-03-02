package com.zhulang.waveedu.edu.query.classquery;

import com.zhulang.waveedu.edu.query.classquery.ClassAttendDayTimeQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 班级每天的安排表
 *
 * @author 狐狸半面添
 * @create 2023-02-27 17:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassWeekPlanQuery {
    /**
     * 星期
     */
    private Integer week;
    /**
     * 时间安排
     */
    private List<ClassAttendDayTimeQuery> times;
}
