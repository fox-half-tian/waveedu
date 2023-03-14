package com.zhulang.waveedu.judge.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.judge.po.ProgramHomeworkProblemCase;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 编程作业问题测试实例表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-13
 */
public interface ProgramHomeworkProblemCaseService extends IService<ProgramHomeworkProblemCase> {

    /**
     * 查询问题所有案例的信息
     *
     * @param problemId 问题id
     * @return 信息列表
     */
    List<HashMap<String, Object>> getProblemCases(Integer problemId);
}
