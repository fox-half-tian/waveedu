package com.zhulang.waveedu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.InviteCodeTypeConstants;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.LessonClassStu;
import com.zhulang.waveedu.edu.dao.LessonClassStuMapper;
import com.zhulang.waveedu.edu.query.LessonClassInviteCodeQuery;
import com.zhulang.waveedu.edu.service.LessonClassService;
import com.zhulang.waveedu.edu.service.LessonClassStuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
        if (lessonClassService.existByUserIdAndClassId(userId, classId)) {
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "已是该班级负责人，邀请码无效");
        }
        // 4.判断是否已经加入了班级
        if (lessonClassStuMapper.exists(new LambdaQueryWrapper<LessonClassStu>()
                .eq(LessonClassStu::getLessonClassId,classId)
                .eq(LessonClassStu::getStuId,userId))){
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "已加入班级，请勿重复操作");
        }
        // 4.加入班级
        LessonClassStu lessonClassStu = new LessonClassStu();
        lessonClassStu.setLessonClassId(classId);
        lessonClassStu.setStuId(userId);
        lessonClassStuMapper.insert(lessonClassStu);
        // 5.加入成功，将 邀请码类型 和 班级id 返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", InviteCodeTypeConstants.LESSON_LESSON_CLASS_CODE_TYPE);
        map.put("classId",classId);
        return Result.ok(map);
    }
}
