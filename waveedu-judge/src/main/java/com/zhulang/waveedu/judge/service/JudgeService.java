package com.zhulang.waveedu.judge.service;

import com.zhulang.waveedu.common.entity.JudgeResult;
import com.zhulang.waveedu.judge.dto.ToJudgeDTO;

/**
 * @author 狐狸半面添
 * @since 2023-03-14
 */
public interface JudgeService {

    /**
     * 判题
     *
     * @param toJudgeDTO 用户判题信息
     * @return 结果
     */
    JudgeResult judge(ToJudgeDTO toJudgeDTO);
}
