package com.zhulang.waveedu.note.dao;

import com.zhulang.waveedu.note.po.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
     * @param id 文件id
     * @param userId 用户id
     * @return 0-不是目录，1-是目录
     */
    Integer selectIsDirByIdAndUserId(@Param("id") Integer id,@Param("userId")  Long userId);
}
