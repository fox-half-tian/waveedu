package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.service.LessonSectionFileService;
import com.zhulang.waveedu.edu.vo.SaveSectionFileVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 课程小节的文件表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-16
 */
@RestController
@RequestMapping("/lesson-section-file")
public class LessonSectionFileController {
    @Resource
    private LessonSectionFileService lessonSectionFileService;


    /**
     * 保存小节的视频资料
     *
     * @param saveSectionFileVO 小节id + 文件名  + 文件加密信息
     * @return 保存成功-返回该资料的Id
     */
    @PostMapping("/save/videoFile")
    public Result saveVideoFile(@Validated @RequestBody SaveSectionFileVO saveSectionFileVO){
        saveSectionFileVO.setUserId(UserHolderUtils.getUserId());
        return lessonSectionFileService.saveVideoFile(saveSectionFileVO);
    }
}
