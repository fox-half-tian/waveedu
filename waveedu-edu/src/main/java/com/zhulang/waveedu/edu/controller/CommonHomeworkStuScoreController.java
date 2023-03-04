package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.CommonHomeworkStuScoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 普通作业表的学生成绩表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-01
 */
@RestController
@RequestMapping("/common-homework-stu-score")
public class CommonHomeworkStuScoreController {
    @Resource
    private CommonHomeworkStuScoreService commonHomeworkStuScoreService;

    /**
     * 教师获取批阅作业任务列表
     *
     * @param classId 班级id
     * @param status 状态，0-未批阅，1-已批阅
     * @return 任务信息列表
     */
    @GetMapping("/get/homeworksCheckTaskList")
    public Result getHomeworksCheckTaskList(@RequestParam("classId") Long classId,
                                            @RequestParam("status") Integer status){
        return commonHomeworkStuScoreService.getHomeworksCheckTaskList(classId,status);
    }

    /**
     * 获取该作业所有学生的完成情况
     *
     * @param homeworkId 作业id
     * @param status 0-未提交，1-批阅中，2-已批阅,3-所有
     * @return 情况列表
     */
    @GetMapping("/get/homeworkStuConditionList")
    public Result getHomeworkStuConditionList(@RequestParam("homeworkId")Integer homeworkId,
                                              @RequestParam(value = "status")Integer status){
        return commonHomeworkStuScoreService.getHomeworkStuConditionList(homeworkId,status);
    }
}
