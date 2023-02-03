package com.zhulang.waveedu.edu.service.impl;

import com.zhulang.waveedu.edu.po.LessonTch;
import com.zhulang.waveedu.edu.dao.LessonTchMapper;
import com.zhulang.waveedu.edu.service.LessonTchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 课程与教学团队的对应表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-03
 */
@Service
public class LessonTchServiceImpl extends ServiceImpl<LessonTchMapper, LessonTch> implements LessonTchService {
    @Resource
    private LessonTchMapper lessonTchMapper;

    @Override
    public boolean isExistByLessonAndUser(Long lessonId, Long userId) {
        return lessonTchMapper.isExistByLessonAndUser(lessonId,userId)!=null;
    }
}
