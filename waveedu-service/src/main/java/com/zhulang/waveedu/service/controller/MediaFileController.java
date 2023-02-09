package com.zhulang.waveedu.service.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.service.service.MediaFileService;
import com.zhulang.waveedu.service.vo.CheckChunkFileVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 第三方服务-媒资文件表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-08
 */
@RestController
@RequestMapping("/media-file")
public class MediaFileController {
    @Resource
    private MediaFileService mediaFileService;


    /**
     * 文件上传前检查文件是否已存在
     *
     * @param object 需要上传的文件的md5值
     * @return 是否存在, false-不存在 true-存在
     */
    @PostMapping("/upload/checkFile")
    public Result checkFile(@RequestBody JSONObject object) {
        return mediaFileService.checkFile(object.getString("fileMd5"));
    }

    /**
     * 分块文件上传前检测分块文件是否已存在
     *
     * @param checkChunkFileVO 分块文件的源文件md5和该文件索引
     * @return 是否存在, false-不存在 true-存在
     */
    @PostMapping("/upload/checkChunk")
    public Result checkChunk(@Validated @RequestBody CheckChunkFileVO checkChunkFileVO) {
        return mediaFileService.checkChunk(checkChunkFileVO.getFileMd5(), checkChunkFileVO.getChunkIndex());
    }

    /**
     * 上传分块文件
     *
     * @param file       分块文件
     * @param fileMd5    原文件md5值
     * @param chunkIndex 分块文件索引
     * @return 上传情况
     */
    @PostMapping("/upload/uploadChunk")
    public Result uploadChunk(@RequestParam("file") MultipartFile file,
                              @RequestParam("fileMd5") @Pattern(regexp = RegexUtils.RegexPatterns.MD5_HEX_REGEX, message = "文件md5格式错误") String fileMd5,
                              @RequestParam("chunkIndex") @Min(value = 0, message = "索引必须大于等于0") Integer chunkIndex) throws Exception {
        return mediaFileService.uploadChunk(fileMd5, chunkIndex, file.getBytes());
    }

    /**
     * 合并分块文件
     *
     * @param fileMd5    文件的md5十六进制值
     * @param fileName   文件名
     * @param tag        文件标签
     * @param chunkTotal 文件块总数
     * @return 合并与上传情况
     */
    @PostMapping("/upload/uploadMergeChunks")
    public Result uploadMergeChunks(@RequestParam("fileMd5") @Pattern(regexp = RegexUtils.RegexPatterns.MD5_HEX_REGEX, message = "文件md5格式错误") String fileMd5,
                                    @RequestParam("fileName") @Pattern(regexp = RegexUtils.RegexPatterns.FILE_NAME_REGEX, message = "文件名最多255个字符") String fileName,
                                    @RequestParam("tag") @Pattern(regexp = RegexUtils.RegexPatterns.FILE_TAG_REGEX, message = "文件标签最多32个字符") String tag,
                                    @RequestParam("chunkTotal") @Min(value = 1, message = "块总数必须大于等于1") Integer chunkTotal) {

        return mediaFileService.uploadMergeChunks(fileMd5, fileName, tag, chunkTotal);
    }
}
