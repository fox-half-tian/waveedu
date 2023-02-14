package com.zhulang.waveedu.edu.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.common.valid.SnowIdValidate;
import com.zhulang.waveedu.edu.service.LessonFileService;
import com.zhulang.waveedu.edu.vo.ModifyFileNameVO;
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
    public Result saveFile(@Validated @RequestBody SaveLessonFileVO saveLessonFileVO) {
        saveLessonFileVO.setUserId(UserHolderUtils.getUserId());
        return lessonFileService.saveFile(saveLessonFileVO);
    }

    /**
     * 删除课程的资料
     *
     * @param object 课程资料id
     * @return 删除状况
     */
    @DeleteMapping("/delFile")
    public Result delFile(@RequestBody JSONObject object) {
        return lessonFileService.removeFile(Long.parseLong(object.getString("lessonFileId")));
    }

    /**
     * 修改资料的文件名
     *
     * @param modifyFileNameVO 文件Id + 文件名
     * @return 修改状况
     */
    @PutMapping("/modify/fileName")
    public Result modifyFileName(@Validated @RequestBody ModifyFileNameVO modifyFileNameVO) {
        return lessonFileService.modifyFileName(modifyFileNameVO.getFileId(), modifyFileNameVO.getFileName());
    }


    /**
     * 获取简单的课程文件信息，主要用于在课程主页展示
     *
     * @param lessonId 课程id
     * @param fileId   文件id
     * @return 文件列表信息：文件id + 文件名 + 文件类型 + 文件格式 + 文件大小 + 上传的时间，按照时间由近到远排序
     */
    @GetMapping("/get/simpleInfoList")
    public Result getSimpleInfoList(
            @RequestParam(value = "lessonId") String lessonId,
            @RequestParam(value = "fileId", required = false) Long fileId
    ) {
        return lessonFileService.getSimpleInfoList(Long.parseLong(lessonId), fileId);
    }
}
