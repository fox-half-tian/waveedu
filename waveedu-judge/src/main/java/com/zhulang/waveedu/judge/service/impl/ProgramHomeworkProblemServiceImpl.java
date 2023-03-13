package com.zhulang.waveedu.judge.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.judge.dao.ProgramHomeworkProblemMapper;
import com.zhulang.waveedu.judge.dto.ProblemLimitInfoDTO;
import com.zhulang.waveedu.judge.po.ProgramHomeworkProblem;
import com.zhulang.waveedu.judge.service.ProgramHomeworkProblemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 编程作业表的题目表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
@Service
public class ProgramHomeworkProblemServiceImpl extends ServiceImpl<ProgramHomeworkProblemMapper, ProgramHomeworkProblem> implements ProgramHomeworkProblemService {
    @Resource
    private ProgramHomeworkProblemMapper programHomeworkProblemMapper;

    @Override
    public ProblemLimitInfoDTO getProblemLimitInfo(Integer problemId) {
        return programHomeworkProblemMapper.selectProblemLimitInfo(problemId);
    }
}
