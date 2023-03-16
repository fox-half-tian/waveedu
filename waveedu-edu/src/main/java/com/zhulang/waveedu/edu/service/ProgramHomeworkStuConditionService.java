package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.ProgramHomeworkStuCondition;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 编程作业的学生情况表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-16
 */
public interface ProgramHomeworkStuConditionService extends IService<ProgramHomeworkStuCondition> {

    /**
     * 添加学生信息到作业情况表
     *
     * @param homeworkId 作业id
     * @param classId 班级id
     */
    void saveStuInfoList(Integer homeworkId, Long classId);

    /**
     * 老师获取所有学生的作业答题情况
     *
     * @param homeworkId 作业id
     * @return 答题情况
     */
    Result tchGetStuCompleteCondition(Integer homeworkId);
}
