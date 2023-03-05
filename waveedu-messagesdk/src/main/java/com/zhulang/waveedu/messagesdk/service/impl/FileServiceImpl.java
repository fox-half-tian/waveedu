package com.zhulang.waveedu.messagesdk.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.messagesdk.dao.FileMapper;
import com.zhulang.waveedu.messagesdk.po.File;
import com.zhulang.waveedu.messagesdk.query.FileIdAndIsDirQuery;
import com.zhulang.waveedu.messagesdk.service.FileContentService;
import com.zhulang.waveedu.messagesdk.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
    @Transactional(rollbackFor = Exception.class)
    public void removeDir(Integer dirId) {
        // 1.获取该目录下所有的直接id和是否为目录
        List<FileIdAndIsDirQuery> infoList = fileMapper.selectIdAndIsDirByParentId(dirId);
        if (infoList == null || infoList.isEmpty()) {
            return;
        }
        // 2.遍历
        for (FileIdAndIsDirQuery info : infoList) {
            // 如果不是目录
            if (info.getIsDir() == 0) {
                removeNoDirFile(info.getId());
            } else {
                // 如果是目录 --> 先删除该目录，再继续递归删除
                fileMapper.deleteById(info.getId());
                removeDir(info.getId());
            }
        }
    }

    public void removeNoDirFile(Integer fileId) {
        fileMapper.deleteById(fileId);
        fileContentService.removeById(fileId);
    }
}
