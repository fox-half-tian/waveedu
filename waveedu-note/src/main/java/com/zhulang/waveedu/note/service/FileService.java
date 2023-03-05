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

    /**
     * 修改文件名（目录名）
     *
     * @param fileId 文件id
     * @param fileName 文件名
     * @return 修改状况
     */
    Result modifyName(Integer fileId, String fileName);

    /**
     * 删除文件或目录
     *
     * @param fileId 文件id
     * @return 删除状况
     */
    Result remove(Integer fileId);

    /**
     * 删除不是目录的文件信息
     *
     * @param fileId 文件id
     */
    void removeNoDirFile(Integer fileId);

    /**
     * 获取该id文件夹下的文件列表信息
     *
     * @param parentId 父id
     * @return 列表信息：文件名 + 是否为目录 + 类型 + 文件id
     */
    Result getListByParentId(Integer parentId);

    /**
     * 通过文件id和用户id查询是否存在
     *
     * @param id 文件id
     * @param userId 用户id
     * @return true-存在，false-不存在
     */
    boolean existsByIdAndUserId(Integer id, Long userId);

    /**
     * 获取该文件id所在目录下的文件列表
     *
     * @param childId 文件id
     * @return 列表信息
     */
    Result getFileAtDirUnderList(Integer childId);

    /**
     * 获取该id文件夹下的所有目录信息
     *
     * @param parentId 父id
     * @return 列表信息：目录名 + id
     */
    Result getDirListByParentId(Integer parentId);
}
