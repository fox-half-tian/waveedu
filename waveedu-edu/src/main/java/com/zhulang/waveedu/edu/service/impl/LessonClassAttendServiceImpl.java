package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.LessonClassAttend;
import com.zhulang.waveedu.edu.dao.LessonClassAttendMapper;
import com.zhulang.waveedu.edu.service.LessonClassAttendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonClassService;
import com.zhulang.waveedu.edu.service.LessonClassStuService;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassAttendVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 课程班级的上课时间表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
@Service
public class LessonClassAttendServiceImpl extends ServiceImpl<LessonClassAttendMapper, LessonClassAttend> implements LessonClassAttendService {
    @Resource
    private LessonClassAttendMapper lessonClassAttendMapper;
    @Resource
    private LessonClassService lessonClassService;
    @Resource
    private LessonClassStuService lessonClassStuService;

    @Override
    public Result saveOne(SaveClassAttendVO saveClassAttendVO) {
        // 1.校验是否为课程创建者
        if (!lessonClassService.existsByUserIdAndClassId(UserHolderUtils.getUserId(), saveClassAttendVO.getLessonClassId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.取出课程名的前后空格
        saveClassAttendVO.setLessonName(saveClassAttendVO.getLessonName().trim());
        // 3.对象属性转换
        LessonClassAttend lessonClassAttend = BeanUtil.copyProperties(saveClassAttendVO, LessonClassAttend.class);
        try {
            // 4.保存
            lessonClassAttendMapper.insert(lessonClassAttend);
            // 5.返回信息Id
            return Result.ok(lessonClassAttend.getId());
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "此时间段已存在上课安排，请勿重复操作");
        }
    }

    @Override
    public Result delOne(Long attendId) {
        // 1.校验数据格式
        if (RegexUtils.isSnowIdInvalid(attendId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "时间信息id格式错误");
        }
        // 2.判断是否为班级创建者
        Long classId = lessonClassAttendMapper.selectLessonClassIdById(attendId);
        if (classId == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "时间信息不存在");
        }
        if (!lessonClassService.existsByUserIdAndClassId(UserHolderUtils.getUserId(), classId)) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 3.删除信息
        lessonClassAttendMapper.deleteById(attendId);
        // 4.返回ok
        return Result.ok();
    }

    @Override
    public Result getClassPlan(Long classId) {
        // 1.校验班级id格式
        if (RegexUtils.isSnowIdInvalid(classId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级id格式错误");
        }
        // 2.判断是否为班级创建者或班级成员
        Long userId = UserHolderUtils.getUserId();
        if (!lessonClassService.existsByUserIdAndClassId(userId, classId) && !lessonClassStuService.existsByClassIdAndUserId(classId, userId)) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 3.判断是否结课
        if (lessonClassService.isEndClassById(classId)){
            return Result.error(HttpStatus.HTTP_INFO_REFUSE.getCode(),"班级已结课");
        }
        // 4.上课时间安排获取
        return Result.ok(lessonClassAttendMapper.selectClassPlan(classId));
    }

    @Override
    public Result getSelfTchPlan() {
        return null;
    }
}
