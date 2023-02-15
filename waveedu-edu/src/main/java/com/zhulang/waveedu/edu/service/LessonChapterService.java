package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonChapter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程章节表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-15
 */
public interface LessonChapterService extends IService<LessonChapter> {

    /**
     * 创建新章节
     *
     * @param lessonId 课程id
     * @param name     章节名
     * @return 创建状况
     */
    Result saveChapter(Long lessonId, String name);

    /**
     * 删除章节
     * 规则；必须先删除章节下的所有小节才能删除该章节
     *
     * @param id 章节id
     * @return 删除结果
     */
    Result removeChapter(Integer id);

    /**
     * 根据章节id获取课程id
     *
     * @param id 章节id
     * @return 课程id
     */
    Long getLessonIdById(Integer id);

    /**
     * 判断是否是教师团队成员，并一带判断章节、课程是否存在
     *
     * @param chapterId 章节id
     * @param userId    用户Id
     * @return null-合法，否则非合法操作
     */
    Result isLessonTch(Integer chapterId, Long userId);

    /**
     * 修改章节的名字
     *
     * @param chapterId   章节id
     * @param chapterName 新的章节name
     * @return 修改状况
     */
    Result modifyChapterName(Integer chapterId, String chapterName);
}
