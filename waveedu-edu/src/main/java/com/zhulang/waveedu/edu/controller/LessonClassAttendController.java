package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonClassAttendService;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassAttendVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * 只允许班级创建者操作
     *
     * @param saveClassAttendVO 班级id,星期，时间，课程名
     * @return 信息id
     */
    @PostMapping("/saveOne")
    public Result saveOne(@Validated @RequestBody SaveClassAttendVO saveClassAttendVO) {
        return lessonClassAttendService.saveOne(saveClassAttendVO);
    }

    /**
     * 删除一份上课时间信息
     * 只允许班级创建者操作
     *
     * @param attendId 信息id
     * @return 删除状况
     */
    @DeleteMapping("/delOne")
    public Result delOne(@RequestParam("attendId") Long attendId) {
        return lessonClassAttendService.delOne(attendId);
    }

    /**
     * 获取该班级的上课时间安排
     * 只允许班级创建者和班级成员获取
     *
     * @param classId 班级id
     * @return 上课时间安排
     */
    @GetMapping("/get/classPlan")
    public Result getClassPlan(@RequestParam("classId") Long classId) {
        return lessonClassAttendService.getClassPlan(classId);
    }

    /**
     * 获取自己的教学安排表
     *
     * @return 个人安排
     */
    @GetMapping("/get/selfTchPlan")
    public Result getSelfTchPlan() {
        return lessonClassAttendService.getSelfTchPlan();
    }

    /**
     * 获得自己的学习课程的计划安排表
     *
     * @return 安排表
     */
    @GetMapping("/get/selfStuPlan")
    public Result getSelfStuPlan() {
        return lessonClassAttendService.getSelfStuPlan();
    }


}
