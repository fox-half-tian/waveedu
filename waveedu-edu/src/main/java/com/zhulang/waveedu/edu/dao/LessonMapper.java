package com.zhulang.waveedu.edu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.po.Lesson;
import com.zhulang.waveedu.edu.query.chapterquery.ChapterNameInfoWithSectionListQuery;
import com.zhulang.waveedu.edu.query.lessonquery.CreateLessonSimpleInfoQuery;
import com.zhulang.waveedu.edu.query.lessonquery.LessonBasicInfoQuery;
import com.zhulang.waveedu.edu.query.lessonquery.LessonCacheQuery;
import com.zhulang.waveedu.edu.query.lessonquery.TchInviteCodeQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程表 Mapper 接口
 *
 * @author 狐狸半面添
 * @create 2023-02-03 16:08
 */
public interface LessonMapper extends BaseMapper<Lesson> {

    /**
     * 查询课程基本信息：课程 id，课程名，课程介绍，课程封面，创建时间，创建人id，创建人头像
     *
     * @param lessonId 课程id
     * @return 课程基本情况
     */
    LessonBasicInfoQuery selectBasicInfo(@Param("lessonId") Long lessonId);


    /**
     * 通过 课程id 获取教师邀请码
     *
     * @param id 课程id
     * @return 教师邀请码与是否禁用
     */
    TchInviteCodeQuery selectTchInviteCodeById(@Param("id") Long id);

    /**
     * 通过课程id获取创建者id
     *
     * @param id 课程id
     * @return 创建者id
     */
    Long selectCreatorIdById(@Param("id") Long id);

    /**
     * 查询用户创建的课程的简单信息，按照时间由近到远进行排序
     * 课程id + 课程名 +  课程封面 + 课程创建时间
     *
     * @param creatorId 创建者id
     * @return 简答信息列表
     */
    List<CreateLessonSimpleInfoQuery> selectCreateLessonSimpleInfoList(@Param("creatorId") Long creatorId);

    /**
     * 查询缓存到redis的lesson信息：课程id + 课程名 + 课程介绍 + 课程封面 + 创建人id + 创建时间
     *
     * @param id lesson主键
     * @return 信息
     */
    LessonCacheQuery selectNeedCacheInfo(@Param("id") Long id);

    /**
     * 查询章节和小节列表信息
     * 包括：章节id，小节id,章节name,小节name
     *
     * @param lessonId 课程id
     * @return 信息列表
     */
    List<ChapterNameInfoWithSectionListQuery> selectChapterAndSectionInfo(@Param("lessonId") Long lessonId);
}
