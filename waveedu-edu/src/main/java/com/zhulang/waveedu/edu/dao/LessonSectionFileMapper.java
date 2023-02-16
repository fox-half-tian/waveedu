package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonSectionFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.SectionFileInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 查询小节文件的信息：文件id,文件名，文件路径，文件格式大小，文件格式
     *
     * @param sectionId 小节id
     * @param type      类型：0-视频 1-其它
     * @return 信息列表，按照时间由远到近
     */
    List<SectionFileInfoQuery> selectFileInfoBySectionIdAndType(@Param("sectionId") Integer sectionId, @Param("type") Integer type);
}
