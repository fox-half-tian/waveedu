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
}
