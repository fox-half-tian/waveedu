package com.zhulang.waveedu.messagesdk.dao;

import com.zhulang.waveedu.messagesdk.po.LessonClassProgramHomework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * 编程作业表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-13
 */
public interface LessonClassProgramHomeworkMapper extends BaseMapper<LessonClassProgramHomework> {

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
