package com.zhulang.waveedu.edu.controller.programhomework;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.ProgramHomeworkStuJudgeService;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SubmitCodeVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 编程作业的学生判题表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/program-homework-stu-judge")
public class ProgramHomeworkStuJudgeController {
    @Resource
    private ProgramHomeworkStuJudgeService programHomeworkStuJudgeService;

    /**
     * 学生提交问题代码，进行判题操作
     *
     * @param submitCodeVO 提交代码信息
     * @param request 请求
     * @return 判题结果
     */
    @PostMapping("/submit")
    public Result submit(@Validated @RequestBody SubmitCodeVO submitCodeVO, HttpServletRequest request){
        return programHomeworkStuJudgeService.submit(submitCodeVO,request);
    }

    /**
     * 学生获取该问题的所有提交记录
     *
     * @param problemId 问题id
     * @return 提交记录列表
     */
    @GetMapping("/getAllSubmitRecords")
    public Result getAllSubmitRecords(@RequestParam("problemId")Integer problemId){
        return programHomeworkStuJudgeService.getAllSubmitRecords(problemId);
    }

    /**
     * 学生获取某一条详细提交记录的信息
     *
     * @param submitId 提交id
     * @return 记录信息
     */
    @GetMapping("/getSubmitRecordDetailInfo")
    public Result getSubmitRecordDetailInfo(@RequestParam("submitId")Integer submitId){
        return programHomeworkStuJudgeService.getSubmitRecordDetailInfo(submitId);
    }
}
