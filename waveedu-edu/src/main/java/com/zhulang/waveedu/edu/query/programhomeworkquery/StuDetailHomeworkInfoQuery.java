package com.zhulang.waveedu.edu.query.programhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-03-15 18:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuDetailHomeworkInfoQuery {
    /**
     * 作业id
     */
    private Integer homeworkId;
    /**
     * 作业标题
     */
    private String homeworkTitle;
    /**
     * 问题数量
     */
    private Integer problemNum;
    /**
     * 完成数量
     */
    private Integer completeNum;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 作业状态
     */
    private Integer homeworkStatus;

    private List<InnerProblemInfo> problemInfoList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerProblemInfo{
        /**
         * 问题id
         */
        private Integer problemId;
        /**
         * 问题标题
         */
        private String problemTitle;
        /**
         * 难度
         */
        private Integer difficulty;
        /**
         * 运行时间(单位ms)
         */
        private Integer runTime;
        /**
         * 运行内存（单位kb）
         */
        private Integer runMemory;
        /**
         * 完成时间
         */
        private LocalDateTime completeTime;
        /**
         * 状态：0-未完成，1-已完成
         */
        private Integer status;
    }
}
