package com.zhulang.waveedu.edu.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 每个时间段的学习安排
 *
 * @author 狐狸半面添
 * @create 2023-02-27 21:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EveryTImeStuPlanQuery {
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
         * 课程名
         */
        private String lessonName;
        /**
         * 教师名
         */
        private String tchName;
    }
}
