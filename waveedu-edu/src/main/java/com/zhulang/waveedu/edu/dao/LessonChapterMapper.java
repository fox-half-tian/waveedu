package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonChapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程章节表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-15
 */
public interface LessonChapterMapper extends BaseMapper<LessonChapter> {

    /**
     * 获取课程的最大章节号
     *
     * @param lessonId 课程id
     * @return 最大章节号
     */
    Integer getMaxOrderByOfLessonId(@Param("lessonId") Long lessonId);

    /**
     * 根据章节id获取课程id
     *
     * @param id 章节id
     * @return 课程id
     */
    Long selectLessonIdById(@Param("id") Integer id);
}
