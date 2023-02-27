package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.edu.service.CommonHomeworkQuestionService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 普通作业表的题目表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/common-homework-question")
public class CommonHomeworkQuestionController {
    @Resource
    private CommonHomeworkQuestionService commonHomeworkQuestionService;

}
