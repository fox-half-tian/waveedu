package com.zhulang.waveedu.note.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.note.po.FileContent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 笔记的文件内容表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-05
 */
public interface FileContentService extends IService<FileContent> {

    /**
     * 获取文件的内容
     *
     * @param fileId 文件id
     * @return 文件类型type + 创建时间 + 修改时间 + 内容 + 文件名
     */
    Result getContent(Integer fileId);
}
