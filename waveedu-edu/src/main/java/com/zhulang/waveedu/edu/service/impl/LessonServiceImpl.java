package com.zhulang.waveedu.edu.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import com.zhulang.waveedu.edu.constant.EduConstants;
import com.zhulang.waveedu.edu.dao.LessonMapper;
import com.zhulang.waveedu.edu.po.Lesson;
import com.zhulang.waveedu.edu.po.LessonTch;
import com.zhulang.waveedu.edu.query.LessonBasicInfoQuery;
import com.zhulang.waveedu.edu.service.LessonService;
import com.zhulang.waveedu.edu.service.LessonTchService;
import com.zhulang.waveedu.edu.vo.SaveLessonVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 课程表 服务实现类
 *
 * @author 狐狸半面添
 * @create 2023-02-03 16:07
 */
@Service
public class LessonServiceImpl extends ServiceImpl<LessonMapper, Lesson> implements LessonService {
    @Resource
    private LessonMapper lessonMapper;
    @Resource
    private LessonTchService lessonTchService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result save(SaveLessonVO saveLessonVO) {
        Lesson lesson = new Lesson();
        Long userId = UserHolderUtils.getUserId();
        // 1.将前端传来的信息放入Lesson对象
        // 1.1 课程名
        lesson.setName(WaveStrUtils.removeBlank(saveLessonVO.getName()));
        // 1.2 介绍信息
        lesson.setIntroduce(WaveStrUtils.removeBlank(saveLessonVO.getIntroduce()));
        // 1.3 头像
        lesson.setCover(saveLessonVO.getCover() != null ? saveLessonVO.getCover() : EduConstants.DEFAULT_LESSON_COVER);

        // 2.设置创建者
        lesson.setCreatorId(userId);
        // 3.生成邀请码
        lesson.setTchInviteCode(RandomUtil.randomString(6));
        // 4.加入到 edu_lesson 表中
        lessonMapper.insert(lesson);
        // 5.加入到 edu_lesson_tch 表中
        LessonTch lessonTch = new LessonTch();
        lessonTch.setLessonId(lesson.getId());
        lessonTch.setUserId(userId);
        lessonTchService.save(lessonTch);
        // 6.返回成功
        return Result.ok(lesson.getId());
    }

    @Override
    public Result getBasicInfo(Long lessonId) {
        if (RegexUtils.isSnowIdInvalid(lessonId)){
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "找不到课程信息");
        }
        LessonBasicInfoQuery info = lessonMapper.selectBasicInfo(lessonId);
        if (info == null) {
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "找不到课程信息");
        }
        return Result.ok(info);
    }
}
