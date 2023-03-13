package com.zhulang.waveedu.judge.controller;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.judge.dto.ToJudgeDTO;
import com.zhulang.waveedu.judge.entity.judge.Judge;
import com.zhulang.waveedu.judge.service.JudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author 狐狸半面添
 * @create 2023-03-10 22:36
 */
@RestController
public class JudgeController {
    @Resource
    private JudgeService judgeService;


    @PostMapping(value = "/judge")
    public Result submitProblemJudge(@RequestBody ToJudgeDTO toJudgeDTO) {
        /*
            1.输入答案的信息
                - submitId：提交序号id
                - pid：问题的id
                - uid：用户的id
                - language：使用的语言，比如 Java
                - code：代码，主类是 Main.java
         */
        Judge judge = toJudgeDTO.getJudge();

        // 2.判断信息是否为空
        if (judge == null || judge.getSubmitId() == null || judge.getUid() == null || judge.getPid() == null) {
            return Result.error("调用参数错误！请检查您的调用参数！");
        }

        // 3.进入判断
        judgeService.judge(judge);

        return Result.ok("判题机评测完成！");
    }
}
