package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.CommonHomeworkStuScore;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

/**
 * <p>
 * 普通作业表的学生成绩表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-01
 */
public interface CommonHomeworkStuScoreService extends IService<CommonHomeworkStuScore> {

    /**
     * 教师获取未批阅作业任务列表
     *
     * @param classId 班级id
     * @param scoreId 分数表id
     * @return 任务信息列表
     */
    Result getHomeworksNoCheckTaskList(Long classId, Integer scoreId);

    /**
     * 获取该作业所有学生的完成情况
     *
     * @param homeworkId 作业id
     * @param status 0-未提交，1-批阅中，2-已批阅，3-所有
     * @return 情况列表
     */
    Result getHomeworkStuConditionList(Integer homeworkId, Integer status);


    /**
     * 根据作业id和学生id获取该学生本次作业的总分数
     *
     * @param homeworkId 作业id
     * @param stuId 学生id
     * @return 学生总分数
     */
    Integer getScoreByHomeworkIdAndStuId(Integer homeworkId, Long stuId);

    /**
     * 根据 作业Id 和 学生id 得到该学生对于作业的状态
     *
     * @param homeworkId 作业id
     * @param stuId      学生id
     * @return 对作业的状态：null-未提交，0-批阅中，1-已提交
     */
    Integer getStatusByHomeworkIdAndStuId(Integer homeworkId, Long stuId);
}
