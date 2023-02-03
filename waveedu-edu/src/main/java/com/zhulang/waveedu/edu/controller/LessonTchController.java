package com.zhulang.waveedu.edu.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.service.LessonTchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 * 课程与教学团队的对应表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-03
 */
@Controller
@RequestMapping("/lesson-tch")
public class LessonTchController {
    @Resource
    private LessonTchService lessonTchService;

    /**
     * 通过邀请码加入教师团队
     *
     * @param object 邀请码
     * @return 是否加入
     */
    @PostMapping("/joinTchTeam")
    private Result joinTchTeam(@RequestBody JSONObject object){
        String code = object.getString("code");
        return lessonTchService.joinTchTeam(code);
    }
}
