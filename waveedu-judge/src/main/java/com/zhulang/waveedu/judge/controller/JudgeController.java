package com.zhulang.waveedu.judge.controller;

import com.zhulang.waveedu.common.entity.JudgeResult;
import com.zhulang.waveedu.judge.dto.ToJudgeDTO;
import com.zhulang.waveedu.judge.service.JudgeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-03-10 22:36
 */
@RestController
public class JudgeController {
    @Resource
    private JudgeService judgeService;


    @PostMapping(value = "/submitProblemJudge")
    public JudgeResult submitProblemJudge(@RequestBody ToJudgeDTO toJudgeDTO) {
        /*
            输入答案的信息
                - problemId：问题的id
                - userId：用户的id
                - language：使用的语言，比如 Java
                - code：代码，主类是 Main.java
         */

        // 进入判断
        return judgeService.judge(toJudgeDTO);
    }
}
