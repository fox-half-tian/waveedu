package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.CommonHomeworkQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
    Map<String,Object> selectHomeworkIsPublishAndCreatorIdById(@Param("id") Integer id);

    /**
     * 根据题目查看作业的发布状况，创建者，作业类型
     *
     * @param id 问题id
     * @return 作业情况
     */
    @SuppressWarnings("MybatisXMapperMethodInspection")
    Map<String,Object> selectHomeworkIsPublishAndCreatorIdAndTypeById(@Param("id") Integer id);
    /**
     * 根据 作业id 查询到该作业的总分数
     *
     * @param commonHomeworkId 作业id
     * @return 作业总分数
     */
    Integer selectTotalScoreByCommonHomeworkId(@Param("commonHomeworkId") Integer commonHomeworkId);

}
