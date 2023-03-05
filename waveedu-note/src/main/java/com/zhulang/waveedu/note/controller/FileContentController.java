package com.zhulang.waveedu.note.controller;


import com.zhulang.waveedu.note.service.FileContentService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
}
