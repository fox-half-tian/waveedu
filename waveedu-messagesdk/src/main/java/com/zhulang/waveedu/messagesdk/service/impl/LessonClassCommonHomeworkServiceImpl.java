package com.zhulang.waveedu.messagesdk.service.impl;

import com.zhulang.waveedu.messagesdk.po.LessonClassCommonHomework;
import com.zhulang.waveedu.messagesdk.dao.LessonClassCommonHomeworkMapper;
import com.zhulang.waveedu.messagesdk.service.LessonClassCommonHomeworkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程班级的普通作业表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-28
 */
@Service
public class LessonClassCommonHomeworkServiceImpl extends ServiceImpl<LessonClassCommonHomeworkMapper, LessonClassCommonHomework> implements LessonClassCommonHomeworkService {
    @Resource
    private LessonClassCommonHomeworkMapper lessonClassCommonHomeworkMapper;

}
