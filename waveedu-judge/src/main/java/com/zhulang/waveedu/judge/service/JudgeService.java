package com.zhulang.waveedu.judge.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.judge.dto.ToJudgeDTO;
import com.zhulang.waveedu.judge.entity.judge.Judge;

import java.util.HashMap;

public interface JudgeService {

    Result judge(ToJudgeDTO toJudgeDTO);
}
