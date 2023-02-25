package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.InviteCodeTypeConstants;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.constant.EduConstants;
import com.zhulang.waveedu.edu.po.LessonClass;
import com.zhulang.waveedu.edu.dao.LessonClassMapper;
import com.zhulang.waveedu.edu.query.ClassBasicInfoQuery;
import com.zhulang.waveedu.edu.query.LessonClassInviteCodeQuery;
import com.zhulang.waveedu.edu.service.LessonClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonTchService;
import com.zhulang.waveedu.edu.vo.classvo.ModifyClassBasicInfoVO;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public LessonClassInviteCodeQuery getInviteCodeById(Long id) {
        return lessonClassMapper.selectInviteCodeById(id);
    }

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

    @Override
    public Result modifyBasicInfo(ModifyClassBasicInfoVO modifyClassBasicInfoVO) {
        Long userId = UserHolderUtils.getUserId();
        // 1.判断是否为该班级的创建者
        Integer exist = lessonClassMapper.existsByClassIdAndUserId(modifyClassBasicInfoVO.getId(), userId);
        if (exist == null) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.如果 name 存在，则去除前后空格
        if (modifyClassBasicInfoVO.getName() != null) {
            if (!StringUtils.hasText(modifyClassBasicInfoVO.getName())) {
                return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级名格式错误");
            }
            modifyClassBasicInfoVO.setName(modifyClassBasicInfoVO.getName().trim());
        }
        // 3.属性转换
        LessonClass lessonClass = BeanUtil.copyProperties(modifyClassBasicInfoVO, LessonClass.class);
        // 4.修改信息
        lessonClassMapper.updateById(lessonClass);
        // 5.返回
        return Result.ok();
    }

    @Override
    public Result modifyInviteCode(Long classId) {
        // 1.判断 classId 格式问题
        if (RegexUtils.isSnowIdInvalid(classId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级id格式错误");
        }
        // 2.判断是否为班级创建者
        Integer exist = lessonClassMapper.existsByClassIdAndUserId(classId, UserHolderUtils.getUserId());
        if (exist == null) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 3.创建新的邀请码
        String code = RandomUtil.randomString(6);
        // 4.修改数据库信息
        LessonClass lessonClass = new LessonClass();
        lessonClass.setId(classId);
        lessonClass.setInviteCode(code);
        lessonClassMapper.updateById(lessonClass);
        // 5.加密返回信息
        return Result.ok(CipherUtils.encrypt(InviteCodeTypeConstants.LESSON_LESSON_CLASS_CODE_TYPE + "-" + classId + "-" + code));
    }

    @Override
    public Result getDetailInfo(Long classId) {
        // 1.校验 classId
        if (RegexUtils.isSnowIdInvalid(classId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级id格式错误");
        }
        // 2.根据 创建者 和 classId 获取班级详细信息
        LambdaQueryWrapper<LessonClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LessonClass::getId, classId)
                .eq(LessonClass::getCreatorId, UserHolderUtils.getUserId());
        LessonClass lessonClass = lessonClassMapper.selectOne(wrapper);
        if (lessonClass == null) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 3.加密邀请码
        lessonClass.setInviteCode(CipherUtils.encrypt(InviteCodeTypeConstants.LESSON_LESSON_CLASS_CODE_TYPE + "-" + classId + "-" + lessonClass.getInviteCode()));
        return Result.ok(lessonClass);
    }

    @Override
    public Result getBasicInfo(Long classId) {
        // 1.校验 classId
        if (RegexUtils.isSnowIdInvalid(classId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级id格式错误");
        }
        // 2.获取班级的基本信息
        ClassBasicInfoQuery query = lessonClassMapper.selectBasicInfo(classId);
        if (query == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "班级信息不存在");
        }
        return Result.ok(query);
    }

    @Override
    public boolean existsByUserIdAndClassId(Long userId, Long classId) {
        return lessonClassMapper.existsByClassIdAndUserId(classId, userId) != null;
    }

    @Override
    public boolean isCreatorByUserIdOfClassId(Long userId, Long classId) {
        return lessonClassMapper.isCreatorByUserIdOfClassId(classId, userId) != null;
    }

    @Override
    public Result delClass(Long classId) {
        // 1.校验
        if (RegexUtils.isSnowIdInvalid(classId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级id格式错误");
        }
        // 2.判断是否为创建者
        if (!this.isCreatorByUserIdOfClassId(UserHolderUtils.getUserId(), classId)) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 3.删除班级
        int result = lessonClassMapper.deleteById(classId);
        if (result == 0) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "班级已不存在");
        }
        return Result.ok();
    }

    @Override
    public void modifyNumOfDynamic(Long classId, String change) {
        lessonClassMapper.updateNumOfDynamic(classId,change);
    }


}
