package com.zhulang.waveedu.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.service.po.MediaFile;
import com.zhulang.waveedu.service.dao.MediaFileMapper;
import com.zhulang.waveedu.service.service.MediaFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * <p>
 * 第三方服务-媒资文件表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-08
 */
@Service
public class MediaFileServiceImpl extends ServiceImpl<MediaFileMapper, MediaFile> implements MediaFileService {
    @Resource
    private MediaFileMapper mediaFileMapper;
    @Resource
    private MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public Result checkFile(String fileMd5) {
        // 1.校验 fileMd5 合法性
        if (RegexUtils.isMd5HexInvalid(fileMd5)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "文件md5格式错误");
        }

        // 2.在文件表存在，并且在文件系统存在，此文件才存在
        // 2.1 判断是否在文件表中存在
        LambdaQueryWrapper<MediaFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MediaFile::getFileMd5, fileMd5)
                .select(MediaFile::getBucket, MediaFile::getFilePath);
        MediaFile mediaFile = mediaFileMapper.selectOne(wrapper);
        if (mediaFile == null) {
            return Result.ok(false);
        }
        // 2.2 判断是否在文件系统存在
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(mediaFile.getBucket()).object(mediaFile.getFilePath()).build();
        try {
            InputStream inputStream = minioClient.getObject(getObjectArgs);
            if (inputStream == null) {
                // 文件不存在
                return Result.ok(false);
            }
        } catch (Exception e) {
            // 文件不存在
            return Result.ok(false);
        }
        // 3.走到这里说明文件已存在，返回true
        return Result.ok(true);
    }

    @Override
    public Result checkChunk(String fileMd5, Integer chunkIndex) {
        // 1.得到分块文件所在目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        // 2.分块文件的路径
        String chunkFilePath = chunkFileFolderPath + chunkIndex;

        // 3.查看是否在文件系统存在
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucket).object(chunkFilePath).build();
        try {
            InputStream inputStream = minioClient.getObject(getObjectArgs);
            if (inputStream == null) {
                //文件不存在
                return Result.ok(false);
            }
        } catch (Exception e) {
            //文件不存在
            return Result.ok(false);
        }

        // 4.走到这里说明文件已存在，返回true
        return Result.ok(true);
    }

    /**
     * 得到分块文件的目录
     *
     * @param fileMd5 文件的md5值
     * @return 分块文件所在目录
     */
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + "chunk" + "/";
    }
}
