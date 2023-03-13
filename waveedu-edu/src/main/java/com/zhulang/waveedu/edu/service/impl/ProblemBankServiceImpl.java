package com.zhulang.waveedu.edu.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.dao.ProblemBankMapper;
import com.zhulang.waveedu.edu.po.ProblemBank;
import com.zhulang.waveedu.edu.query.programhomeworkquery.ProblemCaseInfoQuery;
import com.zhulang.waveedu.edu.query.programhomeworkquery.ProblemDetailInfoQuery;
import com.zhulang.waveedu.edu.service.ProblemBankCaseService;
import com.zhulang.waveedu.edu.service.ProblemBankService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 编程问题题库表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-11
 */
@Service
public class ProblemBankServiceImpl extends ServiceImpl<ProblemBankMapper, ProblemBank> implements ProblemBankService {
    @Resource
    private ProblemBankMapper problemBankMapper;
    @Resource
    private ProblemBankCaseService problemBankCaseService;

    @Override
    public Result getSelfProblemInfoList() {
        return Result.ok(problemBankMapper.selectSelfProblemInfoList(UserHolderUtils.getUserId()));

    }

    @Override
    public Result getPublicProblemInfoList() {
        return Result.ok(problemBankMapper.selectPublicProblemInfoList());
    }

    @Override
    public Result getSelfOrPublicProblemDetailInfo(Integer problemId) {
        // 1.校验格式
        if (problemId < 1000) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "问题id格式错误");
        }
        // 2.获取问题信息
        ProblemDetailInfoQuery questionInfo = problemBankMapper.selectSelfOrPublicProblemDetailInfo(problemId);
        if (questionInfo == null) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "权限不足或问题不存在");
        }
        // 3.获取示例测试
        List<ProblemCaseInfoQuery> caseList = problemBankCaseService.getProblemCaseInfoByProblemId(problemId);

        // 4.将测试案例加入到问题信息中
        questionInfo.setProblemCaseInfoQueryList(caseList);

        // 5.返回
        return Result.ok(questionInfo);
    }
}
