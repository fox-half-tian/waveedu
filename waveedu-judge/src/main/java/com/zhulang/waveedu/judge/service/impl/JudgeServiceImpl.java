package com.zhulang.waveedu.judge.service.impl;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.judge.dto.ProblemLimitInfoDTO;
import com.zhulang.waveedu.judge.dto.ToJudgeDTO;
import com.zhulang.waveedu.judge.judge.JudgeStrategy;
import com.zhulang.waveedu.judge.judge.LanguageConfigLoader;
import com.zhulang.waveedu.judge.service.JudgeService;
import com.zhulang.waveedu.judge.service.ProgramHomeworkProblemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author 狐狸半面添
 * @since 2023-03-14
 * @Description:
 */
@Service
@RefreshScope
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private ProgramHomeworkProblemService programHomeworkProblemService;
    @Resource
    private JudgeStrategy judgeStrategy;


    @Override
    public Result judge(ToJudgeDTO toJudgeDTO) {
        // 标志该判题过程进入编译阶段

        /*
            1.获取问题的信息
                - id：问题id
                - time_limit：时间限制
                - memory_limit：内存限制
                - stack_limit：栈限制
         */
        ProblemLimitInfoDTO problemLimitInfo = programHomeworkProblemService.getProblemLimitInfo(toJudgeDTO.getProblemId());

        // 2.进入编译
        HashMap<String, Object> judgeResult = judgeStrategy.judge(problemLimitInfo, toJudgeDTO);
        /*
            3.返回结果，在正常情况下：
                如果运行正确：返回 code time memory
                如果运行失败：返回 code errMsg
         */
        return Result.ok(judgeResult);
    }
}