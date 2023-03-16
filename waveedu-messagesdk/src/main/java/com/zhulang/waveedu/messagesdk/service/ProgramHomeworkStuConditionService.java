package com.zhulang.waveedu.messagesdk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.messagesdk.po.ProgramHomeworkStuCondition;

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
}
