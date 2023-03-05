package com.zhulang.waveedu.note.service.impl;

import com.zhulang.waveedu.note.po.FileContent;
import com.zhulang.waveedu.note.dao.FileContentMapper;
import com.zhulang.waveedu.note.service.FileContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
