package com.zhulang.waveedu.edu.controller.programhomework;


import com.zhulang.waveedu.edu.service.ProgramHomeworkStuAnswerService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 编程作业的学生正确回答表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-15
 */
@RestController
@RequestMapping("/program-homework-stu-answer")
public class ProgramHomeworkStuAnswerController {
    @Resource
    private ProgramHomeworkStuAnswerService programHomeworkStuAnswerService;

}
