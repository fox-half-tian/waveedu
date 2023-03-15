package com.zhulang.waveedu.edu.controller.programhomework;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.ProgramHomeworkStuJudgeService;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SubmitCodeVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

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
}
