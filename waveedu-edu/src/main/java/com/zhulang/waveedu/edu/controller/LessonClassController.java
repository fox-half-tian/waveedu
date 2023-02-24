package com.zhulang.waveedu.edu.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.HttpStatus;
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
    public Result getDetailInfo(@RequestParam("classId")Long classId){
        return lessonClassService.getDetailInfo(classId);
    }



    /**
     * 获取班级基本信息--》非创建者可以查看的信息
     *
     * @param classId 班级id
     * @return 基本信息
     */
    @GetMapping("/get/basicInfo")
    public Result getBasicInfo(@RequestParam("classId")Long classId){
        return null;
    }


}
