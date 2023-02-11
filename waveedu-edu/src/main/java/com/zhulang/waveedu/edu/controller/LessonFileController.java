package com.zhulang.waveedu.edu.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.service.LessonFileService;
import com.zhulang.waveedu.edu.vo.SaveLessonFileVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 课程学习资料表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-11
 */
@RestController
@RequestMapping("/lesson-file")
public class LessonFileController {
    @Resource
    private LessonFileService lessonFileService;

    /**
     * 上传/保存 课程资料
     *
     * @param saveLessonFileVO 课程id + 文件名 + 文件信息
     * @return 结果
     */
    @PostMapping("/saveFile")
    public Result saveFile(@Validated @RequestBody SaveLessonFileVO saveLessonFileVO){
        saveLessonFileVO.setUserId(UserHolderUtils.getUserId());
        return lessonFileService.saveFile(saveLessonFileVO);
    }

    /**
     * 删除课程的资料
     *
     * @param object 课程id
     * @return 删除状况
     */
    @DeleteMapping("/delFile")
    public Result delFile(@RequestBody JSONObject object){
        return lessonFileService.removeFile(Long.parseLong(object.getString("lessonId")));
    }

}
