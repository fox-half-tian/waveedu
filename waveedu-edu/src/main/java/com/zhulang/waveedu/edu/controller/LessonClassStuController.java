package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonClassService;
import com.zhulang.waveedu.edu.service.LessonClassStuService;
import com.zhulang.waveedu.edu.vo.InviteCodeVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 课程班级与学生的对应关系表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-17
 */
@RestController
@RequestMapping("/lesson-class-stu")
public class LessonClassStuController {
    @Resource
    private LessonClassStuService lessonClassStuService;

    /**
     * 通过邀请码加入班级
     *
     * @param inviteCodeVO 班级id + 班级真实邀请码
     * @return 加入成功，返回班级Id
     */
    @PostMapping("/joinLessonClass")
    public Result  joinLessonClass(@Validated @RequestBody InviteCodeVO inviteCodeVO){
        try {
            return lessonClassStuService.joinLessonClass(inviteCodeVO.getId(),inviteCodeVO.getInviteCode());
        } catch (NumberFormatException e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(),"无效邀请码");
        }
    }
}
