package com.zhulang.waveedu.note.service.impl;

import com.zhulang.waveedu.note.po.File;
import com.zhulang.waveedu.note.dao.FileMapper;
import com.zhulang.waveedu.note.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}
