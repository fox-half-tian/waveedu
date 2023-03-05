package com.zhulang.waveedu.messagesdk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.messagesdk.po.File;

/**
 * <p>
 * 笔记的文件表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-05
 */
public interface FileService extends IService<File> {

    /**
     * 删除目录
     *
     * @param dirId 目录id
     */
    void removeDir(Integer dirId);
}
