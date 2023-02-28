package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.CommonHomeworkQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
    Map<String,Object> selectHomeworkIsPublishAndCreatorIdById(Integer id);
}
