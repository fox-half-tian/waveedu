package com.zhulang.waveedu.edu.service.impl;

import com.zhulang.waveedu.edu.po.LessonClassCommonHomework;
import com.zhulang.waveedu.edu.dao.LessonClassCommonHomeworkMapper;
import com.zhulang.waveedu.edu.service.LessonClassCommonHomeworkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 课程班级的普通作业表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
@Service
public class LessonClassCommonHomeworkServiceImpl extends ServiceImpl<LessonClassCommonHomeworkMapper, LessonClassCommonHomework> implements LessonClassCommonHomeworkService {
    @Resource
    private LessonClassCommonHomeworkMapper lessonClassCommonHomeworkMapper;
}
