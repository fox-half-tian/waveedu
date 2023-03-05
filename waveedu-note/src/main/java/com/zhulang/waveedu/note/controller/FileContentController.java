package com.zhulang.waveedu.note.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.note.service.FileContentService;
import com.zhulang.waveedu.note.vo.SaveFileContentVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 笔记的文件内容表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-05
 */
@RestController
@RequestMapping("/file-content")
public class FileContentController {
    @Resource
    private FileContentService fileContentService;

    /**
     * 获取文件的详细内容
     *
     * @param fileId 文件id
     * @return 文件类型type + 创建时间 + 修改时间 + 内容 + 文件名
     */
    @GetMapping("/getContent")
    public Result getContent(@RequestParam("fileId")Integer fileId){
        return fileContentService.getContent(fileId);
    }

    /**
     * 保存文件内容
     *
     * @param saveFileContentVO 文件id + 内容
     * @return 保存状况
     */
    @PostMapping("/saveContent")
    public Result saveContent(@Validated @RequestBody SaveFileContentVO saveFileContentVO){
        return fileContentService.saveContent(saveFileContentVO);
    }

}
