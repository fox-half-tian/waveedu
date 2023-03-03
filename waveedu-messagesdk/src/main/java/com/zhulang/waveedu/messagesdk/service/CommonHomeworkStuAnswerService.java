package com.zhulang.waveedu.messagesdk.service;

import com.zhulang.waveedu.messagesdk.po.CommonHomeworkStuAnswer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.messagesdk.query.StuQuestionVerifyInfoQuery;

import java.util.List;

/**
 * <p>
 * 普通作业表的学生回答表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-04
 */
public interface CommonHomeworkStuAnswerService extends IService<CommonHomeworkStuAnswer> {

    /**
     * 根据作业id和学生id查询到所有自己答案与问题参考答案、分值的信息
     *
     * @param homeworkId 作业id
     * @param stuId 学生id
     * @return 信息列表
     */
    List<StuQuestionVerifyInfoQuery> getStuQuestionVerifyInfoList(Integer homeworkId, Long stuId);
}
