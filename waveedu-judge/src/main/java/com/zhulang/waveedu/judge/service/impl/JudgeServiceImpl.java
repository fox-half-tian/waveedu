package com.zhulang.waveedu.judge.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.judge.dto.ProblemLimitInfoDTO;
import com.zhulang.waveedu.judge.dto.ToJudgeDTO;
import com.zhulang.waveedu.judge.entity.JudgeResult;
import com.zhulang.waveedu.judge.judge.JudgeStrategy;
import com.zhulang.waveedu.judge.service.JudgeService;
import com.zhulang.waveedu.judge.service.ProgramHomeworkProblemService;
import com.zhulang.waveedu.judge.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author 狐狸半面添
 * @since 2023-03-14
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private ProgramHomeworkProblemService programHomeworkProblemService;
    @Resource
    private JudgeStrategy judgeStrategy;


    @Override
    public JudgeResult judge(ToJudgeDTO toJudgeDTO) {
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
        HashMap<String, Object> judgeResultMap = judgeStrategy.judge(problemLimitInfo, toJudgeDTO);
        /*
            3.返回结果，在正常情况下：
                如果运行正确：返回 code time memory
                如果运行失败：返回 code errMsg
         */
        JudgeResult judgeResult = BeanUtil.fillBeanWithMap(judgeResultMap, new JudgeResult(), false);
        // 设置最大时间和最大空间不超过题目限制时间和空间
        if (judgeResult.getCode().equals(Constants.Judge.STATUS_ACCEPTED.getStatus())) {
            judgeResult.setTime(Math.min(judgeResult.getTime(), problemLimitInfo.getTimeLimit()));
            judgeResult.setMemory(Math.min(judgeResult.getMemory(), problemLimitInfo.getMemoryLimit() * 1024));
        }else{
            String errMsg = judgeResult.getErrMsg();
            if (!StringUtils.hasText(errMsg)){
                errMsg = Constants.Judge.getTypeByStatus(judgeResult.getCode()).getName();
            }
            judgeResult.setErrMsg(errMsg);
        }
        // 返回
        return judgeResult;
    }
}