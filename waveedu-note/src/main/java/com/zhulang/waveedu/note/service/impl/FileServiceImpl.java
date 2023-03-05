package com.zhulang.waveedu.note.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.RabbitConstants;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;

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
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public Result saveFile(SaveFileVO saveFileVO) {
        Long userId = UserHolderUtils.getUserId();
        // 1.如果父目录不是顶级目录，需要先判断父目录是否存在
        if (saveFileVO.getParentId() != 0 && fileMapper.selectCount(new LambdaQueryWrapper<File>()
                .eq(File::getId, saveFileVO.getParentId())
                .eq(File::getUserId, userId)
                .eq(File::getIsDir, 1)) == 0) {
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "父级文件夹不存在");
        }
        // 2.判断是否重名
        if (fileMapper.selectCount(new LambdaQueryWrapper<File>()
                .eq(File::getUserId,userId)
                .eq(File::getParentId,saveFileVO.getParentId())
                .eq(File::getName,saveFileVO.getName()))!=0){
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(),"指定的文件和某个已有的文件重名，请指定其它名称");
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
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "父级文件夹不存在");
        }
        if (fileMapper.selectCount(new LambdaQueryWrapper<File>()
                .eq(File::getUserId,userId)
                .eq(File::getParentId,saveDirVO.getParentId())
                .eq(File::getName,saveDirVO.getName()))!=0){
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(),"指定的文件夹和某个已有的文件重名，请指定其它名称");
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
        try {
            int resultCount = fileMapper.update(null, new LambdaUpdateWrapper<File>()
                    .eq(File::getId, fileId)
                    .eq(File::getUserId, UserHolderUtils.getUserId())
                    .set(File::getName, fileName));
            return resultCount != 0 ? Result.ok() : Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "文件不存在");
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(),"指定的文件名和某个已有的文件重名，请指定其它名称");
        }
    }

    @Override
    public Result remove(Integer fileId) {
        // 1.判断该文件是目录还是文件
        Integer isDir = fileMapper.selectIsDirByIdAndUserId(fileId, UserHolderUtils.getUserId());
        if (isDir == null) {
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "文件不存在");
        }
        // 2.如果是文件，直接删除
        if (isDir == 0) {
            ((FileService) AopContext.currentProxy()).removeNoDirFile(fileId);
            return Result.ok();
        }
        // 3.如果是目录，就将目录以及目录中的内容删除
        // 目录在这里删除，目录中的内容交给 rabbitmq 的工作队列去完成

        fileMapper.deleteById(fileId);
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("dirId",fileId);
        rabbitTemplate.convertAndSend(RabbitConstants.NOTE_DIR_DEL_EXCHANGE_NAME,
                RabbitConstants.NOTE_DIR_DEL_QUEUE_ROUTING_KEY,
                map);
        return Result.ok();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeNoDirFile(Integer fileId) {
        // 1.删除 note_file 表信息
        fileMapper.deleteById(fileId);
        // 2.删除 note_file_content 表信息
        fileContentService.removeById(fileId);
    }
}
