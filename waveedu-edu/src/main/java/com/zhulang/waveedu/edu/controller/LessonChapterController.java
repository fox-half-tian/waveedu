package com.zhulang.waveedu.edu.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonChapterService;
import com.zhulang.waveedu.edu.vo.SaveChapterVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 删除章节
     * 规则；必须先删除章节下的所有小节才能删除该章节
     *
     * @param object 章节id
     * @return 删除结果
     */
    @DeleteMapping("/delChapter")
    public Result delChapter(@RequestBody JSONObject object){
        return lessonChapterService.removeChapter(Integer.parseInt(object.getString("chapterId")));
    }

    /**
     * 获取全部章节和小节的名字信息列表
     *
     * @return 章节+小节 id与name信息
     */
    @GetMapping("/get/chapterSectionNameList")
    public Result getChapterSectionNameList(){
        return null;
    }
}
