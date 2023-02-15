package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonChapterService;
import com.zhulang.waveedu.edu.vo.SaveChapterVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 课程章节表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-15
 */
@RestController
@RequestMapping("/lesson-chapter")
public class LessonChapterController {
    @Resource
    private LessonChapterService lessonChapterService;

    /**
     * 创建新章节
     *
     * @param saveChapterVO 课程id + 章节名
     * @return 创建状况
     */
    @PostMapping("/saveChapter")
    public Result saveChapter(@Validated @RequestBody SaveChapterVO saveChapterVO){
        return lessonChapterService.saveChapter(saveChapterVO.getLessonId(),saveChapterVO.getName());
    }
}
