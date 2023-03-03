package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.CommonHomeworkQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.homeworkquery.*;
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
public interface CommonHomeworkQuestionMapper extends BaseMapper<CommonHomeworkQuestion> {

    /**
     * 根据题目查看作业的发布状况以及创建者
     *
     * @param id 问题id
     * @return 作业情况
     */
    @SuppressWarnings("MybatisXMapperMethodInspection")
    Map<String,Object> selectHomeworkIsPublishAndCreatorIdAndCommonHomeworkIdById(@Param("id") Integer id);

    /**
     * 根据题目查看作业的发布状况，创建者，作业类型
     *
     * @param id 问题id
     * @return 作业情况
     */
    @SuppressWarnings("MybatisXMapperMethodInspection")
    Map<String,Object> selectHomeworkIsPublishAndCreatorIdAndTypeAndHomeworkIdById(@Param("id") Integer id);
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
     * @param stuId 学生id
     * @return 问题id,问题类型，问题描述，问题参考答案，问题解析，问题满分，学生答案
     */
    QuestionDetailAndSelfAnswerWithoutScoreQuery selectQuestionDetailAndSelfAnswerWithoutScore(@Param("homeworkId") Integer homeworkId,
                                                                                               @Param("stuId") Long stuId);

    /**
     * 查询问题简单信息和个人答案，没有自己的答案分数
     *
     * @param homeworkId 作业id
     * @param stuId 学生id
     * @return 问题id,问题类型，问题描述，问题满分，学生答案
     */
    QuestionSimpleAndSelfAnswerWithoutScoreQuery selectQuestionSimpleAndSelfAnswerWithoutScore(@Param("homeworkId") Integer homeworkId,
                                                                                               @Param("stuId") Long stuId);
    /**
     * 查询问题详解和个人答案，有自己的答案分数
     *
     * @param homeworkId 作业id
     * @param stuId 学生id
     * @return 问题id,问题类型，问题描述，问题参考答案，问题解析，问题满分，学生答案，学生分数
     */
    QuestionDetailAndSelfAnswerWithScoreQuery selectQuestionDetailAndSelfAnswerWithScore(@Param("homeworkId") Integer homeworkId,
                                                                                         @Param("stuId") Long stuId);
    /**
     * 查询问题简单信息和个人答案，有自己的答案分数
     *
     * @param homeworkId 作业id
     * @param stuId 学生id
     * @return 问题id,问题类型，问题描述，问题满分，学生答案，学生分数
     */
    QuestionSimpleAndSelfAnswerWithoutScoreQuery selectQuestionSimpleAndSelfAnswerWithScore(@Param("homeworkId") Integer homeworkId,
                                                                                            @Param("stuId") Long stuId);
}
