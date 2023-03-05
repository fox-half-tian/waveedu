package com.zhulang.waveedu.note.dao;

import com.zhulang.waveedu.note.po.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.note.query.SimpleDirInfoQuery;
import com.zhulang.waveedu.note.query.SimpleFileInfoQuery;
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
     * 根据 id 和 用户Id 获取文件是否为目录
     *
     * @param id     文件id
     * @param userId 用户id
     * @return 0-不是目录，1-是目录
     */
    Integer selectIsDirByIdAndUserId(@Param("id") Integer id, @Param("userId") Long userId);

    /**
     * 获取该id文件夹下的文件列表信息
     *
     * @param parentId 父id
     * @param userId   用户id
     * @return 列表信息：文件名 + 是否为目录 + 类型 + 文件Id
     */
    List<SimpleFileInfoQuery> selectSimpleFileInfoList(@Param("parentId") Integer parentId, @Param("userId") Long userId);

    /**
     * 通过文件id和用户id查询是否存在
     *
     * @param id     文件id
     * @param userId 用户id
     * @return true-存在，false-不存在
     */
    Integer existsByIdAndUserId(@Param("id") Integer id, @Param("userId") Long userId);

    /**
     * 查询子文件所在的父目录id
     *
     * @param childId 子文件id
     * @return 父目录id
     */
    Integer selectParentIdById(@Param("childId") Integer childId);

    /**
     * 获取该id文件夹下的目录列表信息
     *
     * @param parentId 父id
     * @param userId   当前用户
     * @return 信息列表：目录名 + 目录id
     */
    List<SimpleDirInfoQuery> selectSimpleDirInfoList(@Param("parentId") Integer parentId, @Param("userId") Long userId);

    /**
     * 查询子目录所在的父目录id
     *
     * @param childId 子目录id
     * @return 父目录id
     */
    Integer selectParentIdByIdAndIsDir(@Param("childId") Integer childId, @Param("isDir") Integer isDir);

    /**
     * 通过目录id和用户id查询是否存在并且为目录
     *
     * @param id 目录id
     * @param userId 用户id
     * @return null-不存在
     */
    Integer existsByIdAndUserIdAndIsDir(@Param("id") Integer id,@Param("userId") Long userId);
}
