package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonSectionService;
import com.zhulang.waveedu.edu.vo.SaveChapterVO;
import com.zhulang.waveedu.edu.vo.SaveSectionVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 课程章节的小节表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-15
 */
@RestController
@RequestMapping("/lesson-section")
public class LessonSectionController {
    @Resource
    private LessonSectionService lessonSectionService;

    /**
     * 创建新小节
     *
     * @param saveSectionVO 章节id + 小节名
     * @return 创建状况
     */
    @PostMapping("/saveSection")
    public Result saveSection(@Validated @RequestBody SaveSectionVO saveSectionVO){
        return lessonSectionService.saveSection(saveSectionVO.getChapterId(),saveSectionVO.getName());
    }

}
