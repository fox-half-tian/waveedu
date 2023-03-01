package com.zhulang.waveedu.messagesdk.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.messagesdk.po.CommonHomeworkQuestion;
import org.apache.ibatis.annotations.Param;

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
     * 根据 作业id 查询到该作业的总分数
     *
     * @param commonHomeworkId 作业id
     * @return 作业总分数
     */
    Integer selectTotalScoreByCommonHomeworkId(@Param("commonHomeworkId") Integer commonHomeworkId);
}
