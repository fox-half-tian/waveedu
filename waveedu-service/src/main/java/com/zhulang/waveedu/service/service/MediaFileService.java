package com.zhulang.waveedu.service.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.service.po.MediaFile;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 第三方服务-媒资文件表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-08
 */
public interface MediaFileService extends IService<MediaFile> {

    /**
     * 文件上传前检查文件是否存在
     *
     * @param fileMd5 需要上传的文件的md5值
     * @return 是否存在, false-不存在 true-存在
     */
    Result checkFile(String fileMd5);

    /**
     * 分块文件上传前检测分块文件是否已存在
     *
     * @param fileMd5 分块文件的源文件md5
     * @param chunkIndex 分块文件索引
     * @return 是否存在, false-不存在 true-存在
     */
    Result checkChunk(String fileMd5, Integer chunkIndex);
}
