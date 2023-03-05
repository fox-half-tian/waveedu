package com.zhulang.waveedu.messagesdk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.messagesdk.dao.FileContentMapper;
import com.zhulang.waveedu.messagesdk.po.FileContent;
import com.zhulang.waveedu.messagesdk.service.FileContentService;
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
}
