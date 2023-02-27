package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.CommonHomeworkQuestionService;
import com.zhulang.waveedu.edu.vo.homework.SaveCommonHomeworkQuestionVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 添加一个题目
     *
     * @param saveCommonHomeworkQuestionVO 题目内容
     * @return 题目id
     */
    @PostMapping("/saveQuestion")
    public Result saveQuestion(@Validated @RequestBody SaveCommonHomeworkQuestionVO saveCommonHomeworkQuestionVO){
        return commonHomeworkQuestionService.saveQuestion(saveCommonHomeworkQuestionVO);
    }

}
