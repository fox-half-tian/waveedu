package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonClassService;
import com.zhulang.waveedu.edu.vo.classvo.ModifyClassBasicInfoVO;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 * 课程班级表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-17
 */
@RestController
@RequestMapping("/lesson-class")
public class LessonClassController {
    @Resource
    private LessonClassService lessonClassService;

    /**
     * 保存课程班级
     *
     * @param saveClassVO 班级名 + 班级封面 + 课程id
     * @return 班级Id
     */
    @PostMapping("/save")
    public Result saveClass(@Validated @RequestBody SaveClassVO saveClassVO){
        return lessonClassService.saveClass(saveClassVO);
    }

    /**
     * 修改班级基本信息：name 和 cover
     *
     * @param modifyClassBasicInfoVO name + cover + id
     * @return 修改情况
     */
    @PutMapping("/modify/basicInfo")
    public Result modifyBasicInfo(@Validated @RequestBody ModifyClassBasicInfoVO modifyClassBasicInfoVO){
        return lessonClassService.modifyBasicInfo(modifyClassBasicInfoVO);
    }

}
