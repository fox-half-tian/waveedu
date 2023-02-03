package com.zhulang.waveedu.edu.controller;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonService;
import com.zhulang.waveedu.edu.vo.SaveLessonVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 创建课程
     *
     * @param saveLessonVO 课程信息
     * @return 情况
     */
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SaveLessonVO saveLessonVO){
        return lessonService.save(saveLessonVO);
    }
}
