package com.zhulang.waveedu.note.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.note.service.FileService;
import com.zhulang.waveedu.note.vo.SaveDirVO;
import com.zhulang.waveedu.note.vo.SaveFileVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 笔记的文件表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-05
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private FileService fileService;

    /**
     * 保存&创建文件
     *
     * @param saveFileVO 文件信息：父目录，文件名，文件类型
     * @return 文件Id
     */
    @PostMapping("/saveFile")
    public Result saveFile(@Validated @RequestBody SaveFileVO saveFileVO){
        return fileService.saveFile(saveFileVO);
    }

    /**
     * 创建目录
     *
     * @param saveDirVO 目录信息：父目录 + 目录名
     * @return 目录id
     */
    @PostMapping("/saveDir")
    public Result saveDir(@Validated @RequestBody SaveDirVO saveDirVO){
        return fileService.saveDir(saveDirVO);
    }


}
