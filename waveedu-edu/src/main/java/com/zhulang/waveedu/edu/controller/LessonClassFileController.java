package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.service.LessonClassFileService;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassFileVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 课程班级资料表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-25
 */
@RestController
@RequestMapping("/lesson-class-file")
public class LessonClassFileController {
    @Resource
    private LessonClassFileService lessonClassFileService;

    /**
     * 上传/保存 班级资料
     *
     * @param saveClassFileVO 班级id + 文件名 + 文件信息
     * @return 资料信息
     */
    @PostMapping("/saveFile")
    public Result saveFile(@Validated @RequestBody SaveClassFileVO saveClassFileVO) {
        saveClassFileVO.setUserId(UserHolderUtils.getUserId());
        return lessonClassFileService.saveFile(saveClassFileVO);
    }
}
