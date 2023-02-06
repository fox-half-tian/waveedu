package com.zhulang.waveedu.edu.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.edu.service.LessonService;
import com.zhulang.waveedu.edu.vo.SaveLessonVO;
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
    public Result getInfo(@NotBlank(message = "课程id不允许为空")
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
}
