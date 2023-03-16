package com.zhulang.waveedu.edu.controller.programhomework;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.ProgramHomeworkStuConditionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 编程作业的学生情况表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-16
 */
@RestController
@RequestMapping("/program-homework-stu-condition")
public class ProgramHomeworkStuConditionController {
    @Resource
    private ProgramHomeworkStuConditionService programHomeworkStuConditionService;
    /**
     * 老师获取所有学生的作业答题情况
     *
     * @param homeworkId 作业id
     * @return 答题情况
     */
    @GetMapping("/tch/getStuCompleteCondition")
    public Result tchGetStuCompleteCondition(@RequestParam("homeworkId")Integer homeworkId){
        return programHomeworkStuConditionService.tchGetStuCompleteCondition(homeworkId);
    }



}
