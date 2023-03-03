package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.edu.service.CommonHomeworkStuAnswerService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 普通作业表的学生回答表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-04
 */
@RestController
@RequestMapping("/common-homework-stu-answer")
public class CommonHomeworkStuAnswerController {
    @Resource
    private CommonHomeworkStuAnswerService commonHomeworkStuAnswerService;
}
