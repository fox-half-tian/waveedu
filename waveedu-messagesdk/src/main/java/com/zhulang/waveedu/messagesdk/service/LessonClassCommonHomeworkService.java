package com.zhulang.waveedu.messagesdk.service;

import com.zhulang.waveedu.messagesdk.po.LessonClassCommonHomework;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 课程班级的普通作业表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-28
 */
public interface LessonClassCommonHomeworkService extends IService<LessonClassCommonHomework> {

    /**
     * 根据作业id，开始时间和发布状态查询是否存在该记录
     *
     * @param id 作业id
     * @param startTime 开始时间
     * @param isPublish 发布状态
     * @return true-存在，false-不存在
     */
    boolean existsByIdAndStartTimeAndIsPublish(Integer id, LocalDateTime startTime,Integer isPublish);
}
