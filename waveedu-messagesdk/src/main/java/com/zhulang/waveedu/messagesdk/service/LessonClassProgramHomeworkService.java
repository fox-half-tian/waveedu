package com.zhulang.waveedu.messagesdk.service;

import com.zhulang.waveedu.messagesdk.po.LessonClassProgramHomework;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 * 编程作业表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-13
 */
public interface LessonClassProgramHomeworkService extends IService<LessonClassProgramHomework> {

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
