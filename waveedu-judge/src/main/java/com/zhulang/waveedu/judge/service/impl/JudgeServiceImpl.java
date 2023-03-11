package com.zhulang.waveedu.judge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhulang.waveedu.judge.dto.TestJudgeReq;
import com.zhulang.waveedu.judge.dto.TestJudgeRes;
import com.zhulang.waveedu.judge.dto.ToJudgeDTO;
import com.zhulang.waveedu.judge.entity.judge.Judge;
import com.zhulang.waveedu.judge.entity.problem.Problem;
import com.zhulang.waveedu.judge.exception.SystemError;
import com.zhulang.waveedu.judge.judge.JudgeContext;
import com.zhulang.waveedu.judge.service.JudgeService;
import com.zhulang.waveedu.judge.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

/**
 * @Author: Himit_ZH
 * @Date: 2022/3/12 15:54
 * @Description:
 */
@Service
@RefreshScope
public class JudgeServiceImpl implements JudgeService {

    @Value("${hoj-judge-server.name}")
    private String name;

    @Resource
    private JudgeContext judgeContext;


    @Override
    public void judge(Judge judge) {
        // 标志该判题过程进入编译阶段

        /*
            1.获取问题的信息
                - id：问题id
                - difficulty：难度
                - time_limit：时间限制
                - memory_limit：内存限制
                - stack_limit：栈限制
                - case_version：实例版本
         */
        QueryWrapper<Problem> problemQueryWrapper = new QueryWrapper<>();
        problemQueryWrapper.select("id",
                        "difficulty",
                        "time_limit",
                        "memory_limit",
                        "stack_limit",
                        "case_version"
                )
                .eq("id", judge.getPid());
        Problem problem = problemEntityService.getOne(problemQueryWrapper);

        // 2.进行判题操作
        Judge finalJudgeRes = judgeContext.Judge(problem, judge);

        // 3.更新该次提交
        judgeEntityService.updateById(finalJudgeRes);

//        if (!Objects.equals(finalJudgeRes.getStatus(), Constants.Judge.STATUS_SUBMITTED_FAILED.getStatus())) {
//            // 更新其它表
//            judgeContext.updateOtherTable(finalJudgeRes.getSubmitId(),
//                    finalJudgeRes.getStatus(),
//                    judge.getCid(),
//                    judge.getUid(),
//                    judge.getPid(),
//                    judge.getGid(),
//                    finalJudgeRes.getScore(),
//                    finalJudgeRes.getTime());
//        }
    }
}