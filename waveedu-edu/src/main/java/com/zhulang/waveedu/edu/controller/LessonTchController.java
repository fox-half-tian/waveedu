package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.edu.service.LessonTchService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 * 课程与教学团队的对应表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-03
 */
@Controller
@RequestMapping("/lesson-tch")
public class LessonTchController {
    @Resource
    private LessonTchService lessonTchService;
}
