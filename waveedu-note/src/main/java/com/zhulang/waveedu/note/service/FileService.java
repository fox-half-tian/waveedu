package com.zhulang.waveedu.note.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.note.po.File;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.note.vo.SaveDirVO;
import com.zhulang.waveedu.note.vo.SaveFileVO;

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
     * 保存&创建文件
     *
     * @param saveFileVO 文件信息：父目录，文件名，文件类型
     * @return 文件Id
     */
    Result saveFile(SaveFileVO saveFileVO);

    /**
     * 保存文件信息到 note_file表 与  note_file_content表
     *
     * @param file 文件
     */
     void saveFileAndContent(File file);

    /**
     * 创建目录
     *
     * @param saveDirVO 目录信息：父目录 + 目录名
     * @return 目录id
     */
    Result saveDir(SaveDirVO saveDirVO);
}
