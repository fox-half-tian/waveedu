package com.zhulang.waveedu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.InviteCodeTypeConstants;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.LessonClassStu;
import com.zhulang.waveedu.edu.dao.LessonClassStuMapper;
import com.zhulang.waveedu.edu.query.LessonClassInviteCodeQuery;
import com.zhulang.waveedu.edu.service.LessonClassService;
import com.zhulang.waveedu.edu.service.LessonClassStuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * <p>
 * 课程班级与学生的对应关系表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-17
 */
@Service
public class LessonClassStuServiceImpl extends ServiceImpl<LessonClassStuMapper, LessonClassStu> implements LessonClassStuService {
    @Resource
    private LessonClassStuMapper lessonClassStuMapper;
    @Resource
    private LessonClassService lessonClassService;

    @Override
    public Result joinLessonClass(Long classId, String inviteCode) {
        // 1.获取班级的邀请码信息
        LessonClassInviteCodeQuery info = lessonClassService.getInviteCodeById(classId);
        // 2.获取为空 或 禁止加入 或 邀请码不对 -> 返回前端
        if (info == null || info.getIsForbidJoin() == 1 || !info.getInviteCode().equals(inviteCode)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效邀请码");
        }
        // 3.判断是否为创建者
        Long userId = UserHolderUtils.getUserId();
        if (lessonClassService.existsByUserIdAndClassId(userId, classId)) {
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "已是该班级负责人，邀请码无效");
        }
        // 4.判断是否已经加入了班级
        if (lessonClassStuMapper.exists(new LambdaQueryWrapper<LessonClassStu>()
                .eq(LessonClassStu::getLessonClassId, classId)
                .eq(LessonClassStu::getStuId, userId))) {
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "已加入班级，请勿重复操作");
        }
        // 4.加入班级
        LessonClassStu lessonClassStu = new LessonClassStu();
        lessonClassStu.setLessonClassId(classId);
        lessonClassStu.setStuId(userId);
        lessonClassStu.setLessonId(info.getLessonId());
        LessonClassStuService proxy = (LessonClassStuService) AopContext.currentProxy();
        proxy.joinClass(lessonClassStu);
        // 5.加入成功，将 邀请码类型 和 班级id 返回
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("type", InviteCodeTypeConstants.LESSON_LESSON_CLASS_CODE_TYPE);
        map.put("id", classId);
        return Result.ok(map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void joinClass(LessonClassStu lessonClassStu) {
        // 班级与学生对应表插入记录
        lessonClassStuMapper.insert(lessonClassStu);
        // 人数加1
        lessonClassService.modifyNumOfDynamic(lessonClassStu.getLessonClassId(), "+ 1");
    }

    @Override
    public Result getJoinClassInfoList(Long userId) {
        return Result.ok(lessonClassStuMapper.selectJoinClassInfoList(userId));
    }

    @Override
    public boolean existsByLessonIdAndUserId(Long lessonId, Long userId) {
        return lessonClassStuMapper.existsByLessonIdAndUserId(lessonId, userId) != null;
    }

    @Override
    public boolean existsByClassIdAndUserId(Long classId, Long userId) {
        return lessonClassStuMapper.existsByClassIdAndUserId(classId, userId) != null;
    }

    @Override
    public Result delStu(Long classId, Long stuId) {
        // 0.校验
        if (RegexUtils.isSnowIdInvalid(classId) || RegexUtils.isSnowIdInvalid(stuId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级id或用户id格式错误");
        }
        Long userId = UserHolderUtils.getUserId();
        // 1.判断是否为该班级的创建者
        if (!lessonClassService.existsByUserIdAndClassId(userId, classId)) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.删除学生
        try {
            LessonClassStuService proxy = (LessonClassStuService) AopContext.currentProxy();
            proxy.exitClass(classId, stuId);
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "班级中已找不到该学生信息");
        }
        // 3.todo 通知

        return Result.ok();
    }

    @Override
    public Result delSelfExit(Long classId) {
        // 1.校验格式
        if (RegexUtils.isSnowIdInvalid(classId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级id格式错误");
        }
        // 2.退出班级
        try {
            this.exitClass(classId, UserHolderUtils.getUserId());
            return Result.ok();
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "班级中已找不到您的信息");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void exitClass(Long classId, Long userId) {
        // 1.从班级与学生对应关系表移除信息
        LambdaQueryWrapper<LessonClassStu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LessonClassStu::getLessonClassId, classId)
                .eq(LessonClassStu::getStuId, userId);
        int result = lessonClassStuMapper.delete(wrapper);
        if (result == 0) {
            throw new RuntimeException();
        }

        // 2.人数减少
        lessonClassService.modifyNumOfDynamic(classId, "- 1");
    }

    @Override
    public Result getStuInfoList(Long classId) {
        // 1.校验格式
        if (RegexUtils.isSnowIdInvalid(classId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级id格式错误");
        }
        // 2.校验身份 --> 只有创建者与班级成员可获取
        Long userId = UserHolderUtils.getUserId();
        if (!lessonClassService.existsByUserIdAndClassId(userId, classId) && !this.existsByClassIdAndUserId(classId, userId)) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }

        // 3.获取学生信息：用户id，用户名，用户头像，学号，身份类型，院校名
        return Result.ok(lessonClassStuMapper.selectStuInfoList(classId));
    }
}
