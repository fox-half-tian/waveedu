package com.zhulang.waveedu.judge.service.impl;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.judge.dto.ProblemLimitInfoDTO;
import com.zhulang.waveedu.judge.dto.ToJudgeDTO;
import com.zhulang.waveedu.judge.entity.judge.Judge;
import com.zhulang.waveedu.judge.judge.JudgeContext;
import com.zhulang.waveedu.judge.judge.JudgeStrategy;
import com.zhulang.waveedu.judge.judge.LanguageConfigLoader;
import com.zhulang.waveedu.judge.judge.entity.LanguageConfig;
import com.zhulang.waveedu.judge.service.JudgeService;
import com.zhulang.waveedu.judge.service.ProgramHomeworkProblemService;
import com.zhulang.waveedu.judge.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

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
    @Resource
    private ProgramHomeworkProblemService programHomeworkProblemService;
    @Resource
    private JudgeStrategy judgeStrategy;
    @Resource
    private LanguageConfigLoader languageConfigLoader;


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

        Judge finalJudgeRes = new Judge();
        // 如果是编译失败、提交错误或者系统错误就有错误提醒
        if (judgeResult.get("code") == Constants.Judge.STATUS_COMPILE_ERROR.getStatus() ||
                judgeResult.get("code") == Constants.Judge.STATUS_SYSTEM_ERROR.getStatus() ||
                judgeResult.get("code") == Constants.Judge.STATUS_RUNTIME_ERROR.getStatus() ||
                judgeResult.get("code") == Constants.Judge.STATUS_SUBMITTED_FAILED.getStatus()||
                judgeResult.get("code") == Constants.Judge.STATUS_LANGUAGE_NO_SUPPORT.getStatus()) {
            finalJudgeRes.setErrorMessage((String) judgeResult.getOrDefault("errMsg", ""));
        }
        // 设置最终结果状态码
        finalJudgeRes.setStatus((Integer) judgeResult.get("code"));
        // 设置最大时间和最大空间不超过题目限制时间和空间
        // kb
        Integer memory = (Integer) judgeResult.get("memory");
        finalJudgeRes.setMemory(Math.min(memory, problemLimitInfo.getMemoryLimit() * 1024));
        // ms
        Integer time = (Integer) judgeResult.get("time");
        finalJudgeRes.setTime(Math.min(time, problemLimitInfo.getTimeLimit()));

        return Result.ok(finalJudgeRes);
    }
}