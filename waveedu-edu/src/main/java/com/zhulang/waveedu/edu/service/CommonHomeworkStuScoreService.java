package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.CommonHomeworkStuScore;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.query.homeworkquery.StuHomeworkSimpleInfoQuery;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Resource;
import java.util.List;
import java.util.PrimitiveIterator;

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
     * 教师获取批阅作业任务列表
     *
     * @param classId 班级id
     * @param status 状态，0-未批阅，1-已批阅
     * @return 任务信息列表
     */
    Result getHomeworksCheckTaskList(Long classId, Integer status);

    /**
     * 获取该作业所有学生的完成情况
     *
     * @param homeworkId 作业id
     * @param status 0-未提交，1-批阅中，2-已批阅，3-所有
     * @return 情况列表
     */
    Result getHomeworkStuConditionList(Integer homeworkId, Integer status);
}
