package com.zhulang.waveedu.edu.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonClassCommonHomeworkService;
import com.zhulang.waveedu.edu.vo.homework.SaveCommonHomeworkVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 * 课程班级的普通作业表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/lesson-class-common-homework")
public class LessonClassCommonHomeworkController {
    @Resource
    private LessonClassCommonHomeworkService lessonClassCommonHomeworkService;

    /**
     * 保存一份作业（未发布）
     *
     * @param saveCommonHomeworkVO 作业信息
     * @return 作业id
     */
    @PostMapping("/saveHomework")
    public Result saveHomework(@Validated @RequestBody SaveCommonHomeworkVO saveCommonHomeworkVO){
        return lessonClassCommonHomeworkService.saveHomework(saveCommonHomeworkVO);
    }

    /**
     * 发布作业
     *
     * @param object 作业id
     * @return 发布状况
     */
    @PostMapping("/publish")
    public Result publish(@RequestBody JSONObject object){
        try {
            return lessonClassCommonHomeworkService.publish(Integer.parseInt(object.getString("commonHomeworkId")));
        } catch (NumberFormatException e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(),"作业id格式错误");
        }
    }
}
