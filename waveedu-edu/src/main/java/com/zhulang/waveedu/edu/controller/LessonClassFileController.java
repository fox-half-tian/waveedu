package com.zhulang.waveedu.edu.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.service.LessonClassFileService;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassFileVO;
import com.zhulang.waveedu.edu.vo.ModifyFileNameVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 删除班级的资料
     *
     * @param lessonClassFileId 班级资料id
     * @return 删除状况
     */
    @DeleteMapping("/delFile")
    public Result delFile(@RequestParam("lessonClassFileId") Long  lessonClassFileId) {
            return lessonClassFileService.removeFile(lessonClassFileId);
    }

    /**
     * 修改资料的文件名
     *
     * @param modifyFileNameVO 文件Id + 文件名
     * @return 修改状况
     */
    @PutMapping("/modify/fileName")
    public Result modifyFileName(@Validated @RequestBody ModifyFileNameVO modifyFileNameVO) {
        return lessonClassFileService.modifyFileName(modifyFileNameVO.getFileId(), modifyFileNameVO.getFileName());
    }
}
