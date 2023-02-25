package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonClassStu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程班级与学生的对应关系表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-17
 */
public interface LessonClassStuService extends IService<LessonClassStu> {

    /**
     * 通过邀请码加入班级
     *
     * @param classId 班级Id
     * @param inviteCode 邀请码
     * @return 加入成功，返回班级Id
     */
    Result joinLessonClass(Long classId, String inviteCode);

    /**
     * 获取所有加入的课程班级的信息
     * 按照加入时间由近及远排序
     *
     * @param userId 用户id
     * @return 班级id + 班级名 + 是否结课 + 课程id + 课程名 + 课程封面
     */
    Result getJoinClassInfoList(Long userId);
}
