package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.edu.service.CommonHomeworkStuScoreService;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/edu/common-homework-stu-score")
public class CommonHomeworkStuScoreController {
    @Resource
    private CommonHomeworkStuScoreService commonHomeworkStuScoreService;
}
