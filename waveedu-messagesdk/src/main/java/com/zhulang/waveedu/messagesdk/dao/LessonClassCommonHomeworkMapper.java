package com.zhulang.waveedu.messagesdk.dao;

import com.zhulang.waveedu.messagesdk.po.LessonClassCommonHomework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程班级的普通作业表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-28
 */
public interface LessonClassCommonHomeworkMapper extends BaseMapper<LessonClassCommonHomework> {

    /**
     *根据 作业id 查询对应的班级学生
     *
     * @param id 作业id
     * @return 学生列表
     */
    List<Long> selectStuIdListById(@Param("id") Integer id);
}
