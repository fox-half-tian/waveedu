package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonClassAttendService;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassAttendVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 课程班级的上课时间表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/lesson-class-attend")
public class LessonClassAttendController {
    @Resource
    private LessonClassAttendService lessonClassAttendService;

    /**
     * 保存一份上课时间信息
     *
     * @param saveClassAttendVO 班级id,星期，时间，课程名
     * @return 信息id
     */
    @PostMapping("/saveOne")
    public Result saveOne(@Validated @RequestBody SaveClassAttendVO saveClassAttendVO){
        return lessonClassAttendService.saveOne(saveClassAttendVO);
    }

}
