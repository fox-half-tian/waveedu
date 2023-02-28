package com.zhulang.waveedu.messagesdk.dao;

import com.zhulang.waveedu.messagesdk.po.LessonClassCommonHomework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
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

    /**
     * 根据作业id，开始时间和发布状态查询是否存在该记录
     *
     * @param id 作业id
     * @param startTime 开始时间
     * @param isPublish 发布状态
     * @return null-不存在，不为空说明存在
     */
    Integer existsByIdAndStartTimeAndIsPublish(@Param("id") Integer id,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("isPublish") Integer isPublish);
}
