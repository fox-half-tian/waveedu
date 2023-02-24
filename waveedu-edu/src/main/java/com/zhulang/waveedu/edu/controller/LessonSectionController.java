package com.zhulang.waveedu.edu.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonSectionService;
import com.zhulang.waveedu.edu.vo.sectionvo.ModifySectionNameVO;
import com.zhulang.waveedu.edu.vo.sectionvo.SaveSectionVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/save/section")
    public Result saveSection(@Validated @RequestBody SaveSectionVO saveSectionVO){
        return lessonSectionService.saveSection(saveSectionVO.getChapterId(),saveSectionVO.getName());
    }

    /**
     * 删除小节
     *
     * @param object 小节id
     * @return 删除状况
     */
    @DeleteMapping("/del/section")
    public Result delSection(@RequestBody JSONObject object){
        try {
            Integer sectionId = Integer.valueOf(object.getString("sectionId"));
            return lessonSectionService.removeSection(sectionId);
        }catch (Exception e){
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(),"小节id格式错误");
        }
    }

    /**
     * 修改小节的名字
     *
     * @param modifySectionNameVO 小节id + 新的小节name
     * @return 修改状况
     */
    @PutMapping("/modify/sectionName")
    public Result modifySectionName(@Validated @RequestBody ModifySectionNameVO modifySectionNameVO){
        return lessonSectionService.modifySectionName(modifySectionNameVO.getSectionId(),modifySectionNameVO.getSectionName());
    }
    
}
