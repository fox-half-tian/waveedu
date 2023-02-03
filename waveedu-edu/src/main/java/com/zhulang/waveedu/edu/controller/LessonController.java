package com.zhulang.waveedu.edu.controller;

import com.zhulang.waveedu.edu.service.LessonService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 与课程相关的统一controller
 *
 * @author 狐狸半面添
 * @create 2023-02-03 0:21
 */
@RestController
@RequestMapping("/lesson")
public class LessonController {
    @Resource
    private LessonService lessonService;
}
