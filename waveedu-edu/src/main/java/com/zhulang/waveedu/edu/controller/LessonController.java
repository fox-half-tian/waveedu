package com.zhulang.waveedu.edu.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.edu.service.LessonService;
import com.zhulang.waveedu.edu.vo.lessonvo.ModifyLessonBasicInfoVO;
import com.zhulang.waveedu.edu.vo.lessonvo.SaveLessonVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 与课程相关的统一controller
 *
 * @author 狐狸半面添
 * @create 2023-02-03 0:21
 */
@RestController
@RequestMapping("/lesson")
public class LessonController {
    @Resource
    private LessonService lessonService;

    /**
     * 创建课程
     *
     * @param saveLessonVO 课程信息
     * @return 情况
     */
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SaveLessonVO saveLessonVO) {
        return lessonService.save(saveLessonVO);
    }

    /**
     * 获取课程基本信息：课程 id，课程名，课程封面，创建时间，创建人id，创建人头像
     *
     * @param lessonId 课程id
     * @return 基本信息
     */
    @GetMapping("/get/basicInfo")
    public Result getBasicInfo(@NotBlank(message = "课程id不允许为空")
                          @Pattern(regexp = RegexUtils.RegexPatterns.SNOW_ID_REGEX, message = "找不到课程信息")
                          @RequestParam("lessonId") Long lessonId) {
        return lessonService.getBasicInfo(lessonId);
    }

    /**
     * 获取教师邀请码
     *
     * @param lessonId 课程id
     * @return 邀请码
     */
    @GetMapping("/get/tchInviteCode")
    public Result getTchInviteCode(@NotBlank(message = "课程id不允许为空")
                                       @Pattern(regexp = RegexUtils.RegexPatterns.SNOW_ID_REGEX, message = "找不到课程信息")
                                       @RequestParam("lessonId") Long lessonId) {
        return lessonService.getTchInviteCode(lessonId);
    }

    /**
     * 修改教师邀请码
     *
     * @param object 课程id
     * @return 修改后的加密邀请码
     */
    @PutMapping("/modify/tchInviteCode")
    public Result modifyTchInviteCode(@RequestBody JSONObject object) {
        Long lessonId = Long.parseLong(object.getString("lessonId"));
        return lessonService.modifyTchInviteCode(lessonId);
    }

    /**
     * 启用/禁用教学邀请码
     * sw：switch的缩写
     *
     * @param object 课程id
     * @return 状态，如果启用，则还会返回教学邀请码
     */
    @PutMapping("/sw/tchInviteCode")
    public Result switchTchInviteCode(@RequestBody JSONObject object) {
        Long lessonId = Long.parseLong(object.getString("lessonId"));
        return lessonService.switchTchInviteCode(lessonId);
    }

    /**
     * 删除课程
     *
     * @param lessonId 课程id
     * @return 状况
     */
    @DeleteMapping("/delLesson")
    public Result delLesson(@RequestParam("lessonId") Long lessonId){
        return lessonService.removeLesson(lessonId);
    }

    /**
     * 获取用户创建的所有课程的简单信息
     *
     * @return 信息列表
     */
    @GetMapping("/get/createLessonSimpleInfoList")
    public Result getCreateLessonSimpleInfoList(){
        return lessonService.getCreateLessonSimpleInfoList();
    }

    /**
     * 修改课程的基本信息
     *
     * @param modifyLessonBasicInfoVO 课程id + 课程名 + 课程介绍 + 课程封面
     * @return 修改后的课程基本信息
     */
    @PutMapping("/modify/lessonBasicInfo")
    public Result modifyLessonBasicInfo(@Validated @RequestBody ModifyLessonBasicInfoVO modifyLessonBasicInfoVO){
        return lessonService.modifyLessonBasicInfo(modifyLessonBasicInfoVO);
    }

    /**
     * 查看当前用户对于该课程的最高身份
     * 0：非课程成员，游客
     * 1：班级普通成员
     * 2：教学团队成员
     * 3：创建者
     *
     * @param lessonId 课程id
     * @return 身份
     */
    @GetMapping("/get/identity")
    public Result getIdentity(@RequestParam("lessonId")Long lessonId){
        return lessonService.getIdentity(lessonId);
    }

    /**
     * 获取课程章节和小节的列表信息
     * 包括：章节id，小节id,章节name,小节name
     *
     * @param lessonId 课程id
     * @return 信息列表
     */
    @GetMapping("/get/chapterAndSectionInfo")
    public Result getChapterAndSectionInfo(@RequestParam("lessonId")Long lessonId){
        return lessonService.getChapterAndSectionInfo(lessonId);
    }
}
