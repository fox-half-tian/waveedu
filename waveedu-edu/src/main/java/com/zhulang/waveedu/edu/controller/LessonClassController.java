package com.zhulang.waveedu.edu.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonClassService;
import com.zhulang.waveedu.edu.vo.classvo.ModifyClassBasicInfoVO;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result saveClass(@Validated @RequestBody SaveClassVO saveClassVO) {
        return lessonClassService.saveClass(saveClassVO);
    }

    /**
     * 修改班级基本信息：班级名 , 封面 ，是否结课，是否禁止加入班级
     *
     * @param modifyClassBasicInfoVO name + cover + isEndClass +isForbidJoin + id
     * @return 修改情况
     */
    @PutMapping("/modify/basicInfo")
    public Result modifyBasicInfo(@Validated @RequestBody ModifyClassBasicInfoVO modifyClassBasicInfoVO) {
        return lessonClassService.modifyBasicInfo(modifyClassBasicInfoVO);
    }

    /**
     * 更换邀请码
     *
     * @param object 班级id
     * @return 新的邀请码
     */
    @PutMapping("/modify/inviteCode")
    public Result modifyInviteCode(@RequestBody JSONObject object) {
        try {
            return lessonClassService.modifyInviteCode(Long.parseLong(object.getString("classId")));
        } catch (NumberFormatException e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级id格式错误");
        }
    }

    /**
     * 获取班级的详细信息，只有创建者可以获取
     *
     * @param classId 班级id
     * @return 详细信息
     */
    @GetMapping("/get/detailInfo")
    public Result getDetailInfo(@RequestParam("classId") Long classId) {
        return lessonClassService.getDetailInfo(classId);
    }

    /**
     * 获取班级基本信息--》都可以查看
     *
     * @param classId 班级id
     * @return 基本信息
     */
    @GetMapping("/get/basicInfo")
    public Result getBasicInfo(@RequestParam("classId") Long classId) {
        return lessonClassService.getBasicInfo(classId);
    }

    /**
     * 删除自己创建的班级
     *
     * @param classId 班级Id
     * @return 删除状况
     */
    @DeleteMapping("/del/class")
    public Result delClass(@RequestParam("classId") Long classId) {
        return lessonClassService.delClass(classId);
    }

    /**
     * 获取创建的班级信息列表
     * 按照时间由近及远排序
     *
     * @param isEndClass 是否结课
     * @param classId    班级id，返回列表信息均小于该id
     * @return 信息列表：班级id,班级名，班级人数,课程封面，课程名，课程id
     */
    @GetMapping("/get/createClassInfoList")
    public Result getCreateClassInfoList(
            @RequestParam(value = "isEndClass") Integer isEndClass,
            @RequestParam(value = "classId", required = false) Long classId) {
        return lessonClassService.getCreateClassInfoList(isEndClass, classId);
    }

    /**
     * 获取该课程的所有班级
     * 教学团队成员可以操作
     * 已按照时间由近到远排序
     *
     * @param lessonId 课程id
     * @return 班级信息:班级id，班级名，学生人数，是否结课，是否禁止加入，创建时间，创建者姓名，邀请码
     */
    @GetMapping("/get/lessonAllClassInfoList")
    public Result getLessonAllClassInfoList(@RequestParam("lessonId")Long lessonId){
        return lessonClassService.getLessonAllClassInfoList(lessonId);
    }

    /**
     * 获取该课程中自己创建的所有班级
     * 教学团队成员可以操作
     * 已按照时间由近到远排序
     *
     * @param lessonId 课程id
     * @return 班级信息:班级id，班级名，学生人数，是否结课，是否禁止加入，创建时间，邀请码
     */
    @GetMapping("/get/lessonSelfAllClassInfoList")
    public Result getLessonSelfAllClassInfoList(@RequestParam("lessonId")Long lessonId){
        return lessonClassService.getLessonSelfAllClassInfoList(lessonId);  
    }

}
