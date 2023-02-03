package com.zhulang.waveedu.edu.controller;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.edu.service.LessonService;
import com.zhulang.waveedu.edu.vo.SaveLessonVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Pattern;

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
    public Result save(@Validated @RequestBody SaveLessonVO saveLessonVO) {
        return lessonService.save(saveLessonVO);
    }

    /**
     * 获取课程基本信息：课程 id，课程名，课程封面，创建时间，创建人id，创建人头像
     *
     * @param lessonId 课程id
     * @return 基本信息
     */
    @GetMapping("/get/basicInfo/{lessonId}")
    public Result getInfo(@PathVariable("lessonId") Long lessonId) {
        return lessonService.getBasicInfo(lessonId);
    }
}
