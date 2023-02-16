package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonSectionFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程小节的文件表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-16
 */
public interface LessonSectionFileMapper extends BaseMapper<LessonSectionFile> {

    /**
     * 通过文件id获取到 sectionId
     *
     * @param id 文件Id
     * @return sectionId
     */
    Integer selectSectionIdById(@Param("id") Integer id);
}
