package com.zhulang.waveedu.edu.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.service.LessonSectionFileService;
import com.zhulang.waveedu.edu.vo.sectionvo.SaveSectionFileVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Validated
public class LessonSectionFileController {
    @Resource
    private LessonSectionFileService lessonSectionFileService;


    /**
     * 保存小节的资料
     *
     * @param saveSectionFileVO 小节id + 文件名  + 文件加密信息
     * @return 保存成功-返回该资料的Id
     */
    @PostMapping("/save/file")
    public Result saveFile(@Validated @RequestBody SaveSectionFileVO saveSectionFileVO) {
        saveSectionFileVO.setUserId(UserHolderUtils.getUserId());
        return lessonSectionFileService.saveFile(saveSectionFileVO);
    }

    /**
     * 删除小节的视频资料
     *
     * @param fileId 资料Id
     * @return 删除状况
     */
    @DeleteMapping("/del/file")
    public Result delVideoFile(@RequestParam("fileId") Integer fileId) {
        return lessonSectionFileService.removeFile(fileId);
    }

    /**
     * 获取某小节的资料列表
     * 信息：文件id,文件名，文件路径，文件格式大小，文件格式
     *
     * @param sectionId 小节id
     * @return 返回两个列表，第一歌列表是视频，第二个列表是资料
     */
    @GetMapping("/get/section/fileList")
    public Result getSectionFileList(@RequestParam("sectionId") Integer sectionId) {
        return lessonSectionFileService.getSectionFileList(sectionId);
    }
}
