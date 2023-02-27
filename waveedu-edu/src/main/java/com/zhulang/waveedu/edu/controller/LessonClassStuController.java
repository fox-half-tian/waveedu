package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.service.LessonClassStuService;
import com.zhulang.waveedu.edu.vo.InviteCodeVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result joinLessonClass(@Validated @RequestBody InviteCodeVO inviteCodeVO) {
        try {
            return lessonClassStuService.joinLessonClass(inviteCodeVO.getId(), inviteCodeVO.getInviteCode());
        } catch (NumberFormatException e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效邀请码");
        }
    }

    /**
     * 获取所有加入的课程班级的信息
     * 按照加入时间由近及远排序
     *
     * @return 班级id + 班级名 + 是否结课 + 课程id + 课程名 + 课程封面
     */
    @GetMapping("/get/self/joinClassInfoList")
    public Result getSelfJoinClassInfoList() {
        return lessonClassStuService.getJoinClassInfoList(UserHolderUtils.getUserId());
    }

    /**
     * 移除学生，只有创建者可以操作
     *
     * @param classId 班级id
     * @param stuId 学生id
     * @return 删除状况
     */
    @DeleteMapping("/del/stu")
    public Result delStu(@RequestParam("classId") Long classId, @RequestParam("stuId") Long stuId) {
        return lessonClassStuService.delStu(classId, stuId);
    }

    /**
     * 退出班级
     *
     * @param classId 班级Id
     * @return 退出情况
     */
    @DeleteMapping("/del/selfExit")
    public Result delSelfExit(@RequestParam("classId") Long classId){
        return lessonClassStuService.delSelfExit(classId);
    }

    /**
     * 获取班级的学生信息列表，只有创建者与班级成员可获取
     * 返回：用户id，用户名，用户头像，学号，身份类型，院校名，手机号
     *
     * @param classId 班级id
     * @return 信息列表
     */
    @GetMapping("/get/stuInfoList")
    public Result getStuInfoList(@RequestParam("classId")Long classId){
        return lessonClassStuService.getStuInfoList(classId);
    }

}
