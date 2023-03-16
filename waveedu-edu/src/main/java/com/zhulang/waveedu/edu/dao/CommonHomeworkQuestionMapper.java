package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.common.component.BatchBaseMapper;
import com.zhulang.waveedu.edu.po.CommonHomeworkQuestion;
import com.zhulang.waveedu.edu.query.commonhomeworkquery.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 普通作业表的题目表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-28
 */
public interface CommonHomeworkQuestionMapper extends BatchBaseMapper<CommonHomeworkQuestion> {

    /**
     * 根据题目查看作业的发布状况以及创建者
     *
     * @param id 问题id
     * @return 作业情况
     */
    @SuppressWarnings("MybatisXMapperMethodInspection")
    Map<String, Object> selectHomeworkIsPublishAndCreatorIdAndCommonHomeworkIdById(@Param("id") Integer id);

    /**
     * 根据题目查看作业的发布状况，创建者，作业类型
     *
     * @param id 问题id
     * @return 作业情况
     */
    @SuppressWarnings("MybatisXMapperMethodInspection")
    Map<String, Object> selectHomeworkIsPublishAndCreatorIdAndTypeAndHomeworkIdById(@Param("id") Integer id);

    /**
     * 根据 作业id 查询到该作业的总分数
     *
     * @param commonHomeworkId 作业id
     * @return 作业总分数
     */
    Integer selectTotalScoreByCommonHomeworkId(@Param("commonHomeworkId") Integer commonHomeworkId);

    /**
     * 根据id查询作业问题普通信息列表
     *
     * @param homeworkId 作业Id
     * @return 主键，题目类型，问题描述，分值
     */
    List<HomeworkQuestionSimpleInfoQuery> selectHomeworkQuestionSimpleInfoList(@Param("homeworkId") Integer homeworkId);

    /**
     * 根据id查询作业问题普通信息列表
     *
     * @param homeworkId 作业Id
     * @return 主键，题目类型，问题描述，分值
     */
    List<HomeworkQuestionDetailInfoQuery> selectHomeworkQuestionDetailInfoList(@Param("homeworkId") Integer homeworkId);


    /**
     * 查询问题详解和个人答案，没有自己的答案分数
     *
     * @param homeworkId 作业id
     * @param stuId      学生id
     * @return 问题id, 问题类型，问题描述，问题参考答案，问题解析，问题满分，学生答案
     */
    List<QuestionDetailAndSelfAnswerWithoutScoreQuery> selectQuestionDetailAndSelfAnswerWithoutScore(@Param("homeworkId") Integer homeworkId,
                                                                                                     @Param("stuId") Long stuId);

    /**
     * 查询问题简单信息和个人答案，没有自己的答案分数
     *
     * @param homeworkId 作业id
     * @param stuId      学生id
     * @return 问题id, 问题类型，问题描述，问题满分，学生答案
     */
    List<QuestionSimpleAndSelfAnswerWithoutScoreQuery> selectQuestionSimpleAndSelfAnswerWithoutScore(@Param("homeworkId") Integer homeworkId,
                                                                                                     @Param("stuId") Long stuId);

    /**
     * 查询问题详解和个人答案，有自己的答案分数
     *
     * @param homeworkId 作业id
     * @param stuId      学生id
     * @return 问题id, 问题类型，问题描述，问题参考答案，问题解析，问题满分，学生答案，学生分数
     */
    List<QuestionDetailAndSelfAnswerWithScoreQuery> selectQuestionDetailAndSelfAnswerWithScore(@Param("homeworkId") Integer homeworkId,
                                                                                               @Param("stuId") Long stuId);

    /**
     * 查询问题简单信息和个人答案，有自己的答案分数
     *
     * @param homeworkId 作业id
     * @param stuId      学生id
     * @return 问题id, 问题类型，问题描述，问题满分，学生答案，学生分数
     */
    List<QuestionSimpleAndSelfAnswerWithoutScoreQuery> selectQuestionSimpleAndSelfAnswerWithScore(@Param("homeworkId") Integer homeworkId,
                                                                                                  @Param("stuId") Long stuId);

    /**
     * 根据问题的id查询作业id,类型,截止时间，状态
     *
     * @param id 问题id
     * @return 作业类型与id
     */
    HomeworkIdAndTypeAndEndTimeAndIsPublishQuery selectHomeworkIdAndTypeAndEndTimeAndIsPublishById(@Param("id") Integer id);

    /**
     * 判断该问题的创建者是否为该用户
     *
     * @param questionId 问题id
     * @param userId     用户id
     * @return null-不是，非null-是
     */
    Integer isHomeworkCreatorByQuestionIdAndUserId(@Param("questionId") Integer questionId,
                                                   @Param("userId") Long userId);

    /**
     * 通过 questionId 获取该问题的作业id
     *
     * @param questionId 问题id
     * @return 作业id
     */
    Integer selectHomeworkIdByQuestionId(@Param("questionId") Integer questionId);

    /**
     * 查询是否属于同一份作业
     *
     * @param questionIds 问题ids
     * @return 1-为同一份作业
     */
    Integer selectHomeworkIdByQuestionIds(@Param("ids") List<Integer> questionIds);
}
