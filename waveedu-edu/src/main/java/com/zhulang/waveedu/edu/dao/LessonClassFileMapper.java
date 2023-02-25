package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClassFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程班级资料表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-25
 */
public interface LessonClassFileMapper extends BaseMapper<LessonClassFile> {

    /**
     * 根据 文件Id 获取 文件所属班级
     *
     * @param id 文件id
     * @return 班级Id
     */
    Long selectLessonClassId(@Param("id") Long id);
}
