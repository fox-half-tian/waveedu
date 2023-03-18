package com.zhulang.waveedu.chat.controller;


import com.zhulang.waveedu.chat.service.EduLessonClassService;
import com.zhulang.waveedu.common.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 课程班级表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-18
 */
@RestController
@RequestMapping("/edu-lesson-class")
public class EduLessonClassController {
    @Resource
    private EduLessonClassService lessonClassService;

    /**
     * 获取所有管理与加入的班级列表
     * 这里功能不完善，只写了加入的班级
     *
     * @return 列表
     */
    @GetMapping("/getClassInfoList")
    public Result getClassInfoList(){
        return lessonClassService.getClassInfoList();
    }
}
