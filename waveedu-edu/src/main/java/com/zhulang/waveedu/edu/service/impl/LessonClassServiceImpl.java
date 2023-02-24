package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.constant.EduConstants;
import com.zhulang.waveedu.edu.po.LessonClass;
import com.zhulang.waveedu.edu.dao.LessonClassMapper;
import com.zhulang.waveedu.edu.service.LessonClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonTchService;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 课程班级表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-17
 */
@Service
public class LessonClassServiceImpl extends ServiceImpl<LessonClassMapper, LessonClass> implements LessonClassService {

    @Resource
    private LessonClassMapper lessonClassMapper;
    @Resource
    private LessonTchService lessonTchService;

    @Override
    public Result saveClass(SaveClassVO saveClassVO) {
        Long userId = UserHolderUtils.getUserId();
        // 1.判断课程是否存在与该用户是否为教师
        Result result = lessonTchService.isLessonTch(saveClassVO.getLessonId(), userId);
        if (result != null) {
            return result;
        }
        // 2.判断封面是否为空 --> 为空则使用默认
        if (saveClassVO.getCover() == null) {
            saveClassVO.setCover(EduConstants.DEFAULT_LESSON_CLASS_COVER);
        }
        // 3.对象转换
        LessonClass lessonClass = BeanUtil.copyProperties(saveClassVO, LessonClass.class);
        // 4.生成邀请码
        lessonClass.setInviteCode(RandomUtil.randomString(6));
        // 5.设置创建者
        lessonClass.setCreatorId(userId);
        // 6.取出班级名前后空格
        lessonClass.setName(lessonClass.getName().trim());
        // 6.保存
        lessonClassMapper.insert(lessonClass);
        // 7.返回班级id
        return Result.ok(lessonClass.getId());
    }
}
