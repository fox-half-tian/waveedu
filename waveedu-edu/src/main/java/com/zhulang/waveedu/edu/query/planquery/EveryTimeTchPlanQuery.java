package com.zhulang.waveedu.edu.query.planquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 每个时间段的教学安排
 *
 * @author 狐狸半面添
 * @create 2023-02-27 18:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EveryTimeTchPlanQuery {
    /**
     * 星期
     */
    private Integer week;
    /**
     * 时间段
     */
    private Integer time;
    /**
     * 每个时间段的教学课程
     */
    private List<PlanLesson> planLessons;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanLesson{
        /**
         * 班级名
         */
        private String className;
        /**
         * 课程名
         */
        private String lessonName;
    }
}
