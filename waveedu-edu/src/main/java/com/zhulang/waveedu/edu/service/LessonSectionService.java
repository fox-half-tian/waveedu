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

    /**
     * 修改小节的名字
     *
     * @param sectionId   小节id
     * @param sectionName 新的小节name
     * @return 修改状况
     */
    Result modifySectionName(Integer sectionId, String sectionName);

    /**
     * 判断是否是教师团队成员，并一带判断小节、章节、课程是否存在
     *
     * @param sectionId 小节id
     * @param userId 用户Id
     * @return null-合法，否则非合法操作
     */
    Result isLessonTch(Integer sectionId,Long userId);

    /**
     * 判断是否存在为该章节id的小节
     *
     * @param chapterId 章节Id
     * @return true-存在，false-不存在
     */
    boolean existSectionByChapterId(Integer chapterId);
}
