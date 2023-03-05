package com.zhulang.waveedu.messagesdk.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.messagesdk.po.File;
import com.zhulang.waveedu.messagesdk.query.FileIdAndIsDirQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 笔记的文件表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-05
 */
public interface FileMapper extends BaseMapper<File> {

    /**
     * 获取该id下所有的文件id和是否为目录
     *
     * @param parentId 父目录
     * @return id + isDir
     */
    List<FileIdAndIsDirQuery> selectIdAndIsDirByParentId(@Param("parentId") Integer parentId);
}
