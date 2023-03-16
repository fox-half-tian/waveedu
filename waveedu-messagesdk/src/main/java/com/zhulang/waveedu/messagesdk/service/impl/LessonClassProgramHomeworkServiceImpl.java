package com.zhulang.waveedu.messagesdk.service.impl;

import com.zhulang.waveedu.messagesdk.po.LessonClassProgramHomework;
import com.zhulang.waveedu.messagesdk.dao.LessonClassProgramHomeworkMapper;
import com.zhulang.waveedu.messagesdk.service.LessonClassProgramHomeworkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 编程作业表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-13
 */
@Service
public class LessonClassProgramHomeworkServiceImpl extends ServiceImpl<LessonClassProgramHomeworkMapper, LessonClassProgramHomework> implements LessonClassProgramHomeworkService {
    @Resource
    private LessonClassProgramHomeworkMapper lessonClassProgramHomeworkMapper;
    @Override
    public boolean existsByIdAndStartTimeAndIsPublish(Integer id, LocalDateTime startTime, Integer isPublish) {
        return lessonClassProgramHomeworkMapper.existsByIdAndStartTimeAndIsPublish(id, startTime, isPublish) != null;
    }
}
