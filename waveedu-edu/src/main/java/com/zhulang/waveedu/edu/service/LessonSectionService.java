package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonSection;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程章节的小节表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-15
 */
public interface LessonSectionService extends IService<LessonSection> {

    /**
     * 创建新小节
     *
     * @param chapterId 章节id
     * @param name      小节名
     * @return 创建状况
     */
    Result saveSection(Integer chapterId, String name);

    /**
     * 删除小节
     *
     * @param sectionId 小节id
     * @return 删除状况
     */
    Result removeSection(Integer sectionId);
}
