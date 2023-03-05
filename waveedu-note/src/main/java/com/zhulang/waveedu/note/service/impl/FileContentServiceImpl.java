package com.zhulang.waveedu.note.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.note.po.FileContent;
import com.zhulang.waveedu.note.dao.FileContentMapper;
import com.zhulang.waveedu.note.query.FileContentQuery;
import com.zhulang.waveedu.note.service.FileContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.note.service.FileService;
import com.zhulang.waveedu.note.vo.SaveFileContentVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 笔记的文件内容表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-05
 */
@Service
public class FileContentServiceImpl extends ServiceImpl<FileContentMapper, FileContent> implements FileContentService {
    @Resource
    private FileContentMapper fileContentMapper;
    @Resource
    private FileService fileService;

    @Override
    public Result getContent(Integer fileId) {
        // 1.校验格式
        if (fileId < 1) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "文件id格式错误");
        }
        // 2.获取信息
        FileContentQuery info = fileContentMapper.selectFileContentById(fileId, UserHolderUtils.getUserId());
        if (info == null) {
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "找不到文件内容信息");
        }
        // 3.返回
        return Result.ok(info);
    }

    @Override
    public Result saveContent(SaveFileContentVO saveFileContentVO) {
        // 1.查看权限
        if (!fileService.existsByIdAndUserId(saveFileContentVO.getFileId(), UserHolderUtils.getUserId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.保存
        int updateCount = fileContentMapper.update(null, new LambdaUpdateWrapper<FileContent>()
                .eq(FileContent::getId, saveFileContentVO.getFileId())
                .set(FileContent::getContent, saveFileContentVO.getContent()));
        // 3.返回
        return updateCount != 0 ? Result.ok() : Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "操作失败");
    }
}
