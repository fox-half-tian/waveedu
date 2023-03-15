package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.edu.po.ProgramHomeworkStuAnswer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 编程作业的学生正确回答表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-15
 */
public interface ProgramHomeworkStuAnswerService extends IService<ProgramHomeworkStuAnswer> {

    /**
     * 根据问题id和用户id查询是否存在正确答案
     *
     * @param userId 用户id
     * @param problemId 问题id
     * @return 正确答案id
     */
    Integer getIdByUserIdAndProblemId(Long userId, Integer problemId);
}
