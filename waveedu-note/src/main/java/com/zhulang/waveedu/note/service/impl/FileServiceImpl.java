package com.zhulang.waveedu.note.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.note.po.File;
import com.zhulang.waveedu.note.dao.FileMapper;
import com.zhulang.waveedu.note.po.FileContent;
import com.zhulang.waveedu.note.service.FileContentService;
import com.zhulang.waveedu.note.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.note.vo.SaveDirVO;
import com.zhulang.waveedu.note.vo.SaveFileVO;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 笔记的文件表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-05
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
    @Resource
    private FileMapper fileMapper;
    @Resource
    private FileContentService fileContentService;

    @Override
    public Result saveFile(SaveFileVO saveFileVO) {
        Long userId = UserHolderUtils.getUserId();
        // 1.如果父目录不是顶级目录，需要先判断父目录是否存在
        if (saveFileVO.getParentId() != 0 && fileMapper.selectCount(new LambdaQueryWrapper<File>()
                .eq(File::getId, saveFileVO.getParentId())
                .eq(File::getUserId, userId)
                .eq(File::getIsDir, 1)) == 0) {
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "父级目录不存在");
        }
        // 属性转换与赋值
        File file = BeanUtil.copyProperties(saveFileVO, File.class);
        file.setIsDir(0);
        file.setUserId(userId);
        // 保存
        ((FileService) AopContext.currentProxy()).saveFileAndContent(file);
        // 返回文件id
        return Result.ok(file.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveFileAndContent(File file) {
        // 保存
        fileMapper.insert(file);
        // 内容表信息
        FileContent fileContent = new FileContent();
        fileContent.setId(file.getId());
        fileContentService.save(fileContent);
    }

    @Override
    public Result saveDir(SaveDirVO saveDirVO) {
        Long userId = UserHolderUtils.getUserId();
        // 1.如果父目录不是顶级目录，需要先判断父目录是否存在
        if (saveDirVO.getParentId() != 0 && fileMapper.selectCount(new LambdaQueryWrapper<File>()
                .eq(File::getId, saveDirVO.getParentId())
                .eq(File::getUserId, userId)
                .eq(File::getIsDir, 1)) == 0) {
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "父级目录不存在");
        }
        // 2.属性转换与赋值
        File dir = BeanUtil.copyProperties(saveDirVO, File.class);
        dir.setUserId(userId);
        dir.setIsDir(1);
        // 3.保存
        fileMapper.insert(dir);
        // 4.返回
        return Result.ok(dir.getId());
    }

    @Override
    public Result modifyName(Integer fileId, String fileName) {
        // 1.校验格式
        if (fileId < 1) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "文件Id格式错误");
        }
        if (StrUtil.isBlank(fileName)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效文件名");
        }
        if (fileName.length() > 64) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "文件名最多64字");
        }
        // 2.修改文件名
        int resultCount = fileMapper.update(null, new LambdaUpdateWrapper<File>()
                .eq(File::getId, fileId)
                .eq(File::getUserId, UserHolderUtils.getUserId())
                .set(File::getName, fileName));
        return resultCount != 0 ? Result.ok() : Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "文件不存在");
    }
}
