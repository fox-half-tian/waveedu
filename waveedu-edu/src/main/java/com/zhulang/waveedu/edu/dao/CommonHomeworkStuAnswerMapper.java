package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.common.component.BatchBaseMapper;
import com.zhulang.waveedu.edu.po.CommonHomeworkStuAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 普通作业表的学生回答表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-04
 */
public interface CommonHomeworkStuAnswerMapper extends BatchBaseMapper<CommonHomeworkStuAnswer> {


    /**
     * 批量删除所有该学生的对应问题id的信息
     *
     * @param stuId       学生id
     * @param questionIds 问题id列表
     */
    void deleteBatchByStuIdAndQuestionIds(@Param("stuId") Long stuId, @Param("ids") List<Integer> questionIds);


}
