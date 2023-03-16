package com.zhulang.waveedu.judge.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.judge.dto.ProblemLimitInfoDTO;
import com.zhulang.waveedu.judge.po.ProgramHomeworkProblem;

/**
 * <p>
 * 编程作业表的题目表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
public interface ProgramHomeworkProblemService extends IService<ProgramHomeworkProblem> {

    /**
     * 获取问题的限制信息：内存限制，时间限制，栈限制
     *
     * @param problemId 问题id
     * @return 信息
     */
    ProblemLimitInfoDTO getProblemLimitInfo(Integer problemId);
}
