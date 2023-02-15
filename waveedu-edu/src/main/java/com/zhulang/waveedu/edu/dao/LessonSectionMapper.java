package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonSection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程章节的小节表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-15
 */
public interface LessonSectionMapper extends BaseMapper<LessonSection> {

    /**
     * 获取当前章节最新的小节
     *
     * @param chapterId 章节id
     * @return 小节序号
     */
    Integer getMaxOrderByOfChapterId(Integer chapterId);

    /**
     * 获取当前小节的章节id
     *
     * @param sectionId 小节id
     * @return 章节id
     */
    Integer selectChapterIdById(Integer sectionId);
}
