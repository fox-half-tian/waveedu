package com.zhulang.waveedu.note.dao;

import com.zhulang.waveedu.note.po.FileContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.note.query.FileContentQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 笔记的文件内容表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-05
 */
public interface FileContentMapper extends BaseMapper<FileContent> {

    /**
     * 获取文件的内容
     *
     * @param id 文件Id
     * @param userId 用户id
     * @return 文件类型type + 创建时间 + 修改时间 + 内容 + 文件名
     */
    FileContentQuery selectFileContentById(@Param("id") Integer id,@Param("userId")Long userId);
}
