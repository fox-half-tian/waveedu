package com.zhulang.waveedu.judge.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.judge.dao.ProgramHomeworkProblemCaseMapper;
import com.zhulang.waveedu.judge.dao.ProgramHomeworkProblemMapper;
import com.zhulang.waveedu.judge.po.ProgramHomeworkProblemCase;
import com.zhulang.waveedu.judge.service.ProgramHomeworkProblemCaseService;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 编程作业问题测试实例表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-13
 */
@Service
public class ProgramHomeworkProblemCaseServiceImpl extends ServiceImpl<ProgramHomeworkProblemCaseMapper, ProgramHomeworkProblemCase> implements ProgramHomeworkProblemCaseService {
    @Resource
    private ProgramHomeworkProblemMapper programHomeworkProblemMapper;

    @Override
    public List<HashMap<String, Object>> getProblemCases(Integer problemId) {
        return programHomeworkProblemMapper.selectProblemCases(problemId);
    }
}
