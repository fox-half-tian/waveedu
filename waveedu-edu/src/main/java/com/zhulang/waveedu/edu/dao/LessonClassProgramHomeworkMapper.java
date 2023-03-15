package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClassProgramHomework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.programhomeworkquery.HomeworkIsPublishAndEndTimeQuery;
import com.zhulang.waveedu.edu.query.programhomeworkquery.TchSimpleHomeworkInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 编程作业表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
public interface LessonClassProgramHomeworkMapper extends BaseMapper<LessonClassProgramHomework> {

    /**
     * 作业状态
     *
     * @param homeworkId 作业id
     * @param creatorId 创建者id
     * @return 0-未发布，1-已发布，2-发布中
     */
    Integer selectIsPublishByHomeworkIdAndCreatorId(@Param("homeworkId") Integer homeworkId, @Param("creatorId") Long creatorId);

    /**
     * 设置正确的题目数量
     *
     * @param homeworkId 作业id
     */
    void updateNumById(@Param("homeworkId") Integer homeworkId);

    /**
     * 是否存在作业的创建者
     *
     * @param homeworkId 作业id
     * @param creatorId 创建者id
     * @return null-不存在
     */
    Integer existsByHomeworkIdAndCreatorId(@Param("homeworkId") Integer homeworkId, @Param("creatorId") Long creatorId);

    /**
     * 根据作业状态查询作业信息
     *
     * @param classId 班级id
     * @param status 状态
     * @return 信息列表
     */
    List<TchSimpleHomeworkInfoQuery> selectTchHomeworkInfoList(@Param("classId") Long classId, @Param("status") Integer status);

    /**
     * 根据 作业id 查询数量
     *
     * @param id 作业id
     * @return 数量
     */
    long selectNumById(@Param("id") Integer id);

    /**
     * 根据问题id查询作业的发布状况与截止时间
     *
     * @param problemId 问题id
     * @return 信息
     */
    HomeworkIsPublishAndEndTimeQuery selectIsPublishAndEndTimeByProblemId(@Param("problemId") Integer problemId);
}
