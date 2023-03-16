package com.zhulang.waveedu.edu.service.impl;

import com.zhulang.waveedu.edu.dao.ProblemBankCaseMapper;
import com.zhulang.waveedu.edu.po.ProblemBankCase;
import com.zhulang.waveedu.edu.query.programhomeworkquery.ProblemCaseInfoQuery;
import com.zhulang.waveedu.edu.service.ProblemBankCaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 编程问题题库测试实例表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-13
 */
@Service
public class ProblemBankCaseServiceImpl extends ServiceImpl<ProblemBankCaseMapper, ProblemBankCase> implements ProblemBankCaseService {
    @Resource
    private ProblemBankCaseMapper problemBankCaseMapper;

    @Override
    public List<ProblemCaseInfoQuery> getProblemCaseInfoByProblemId(Integer problemId) {
        return problemBankCaseMapper.selectProblemCaseInfoByProblemId(problemId);
    }
}
