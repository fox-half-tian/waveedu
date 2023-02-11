package com.zhulang.waveedu.edu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.Lesson;
import com.zhulang.waveedu.edu.po.LessonTch;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程与教学团队的对应表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-03
 */
public interface LessonTchService extends IService<LessonTch> {
    /**
     * 通过 lessonId 和 userId 查询该用户是否为该课程的教师
     *
     * @param lessonId 课程id
     * @param userId 用户id
     * @return false不存在，true存在
     */
    boolean isExistByLessonAndUser(Long lessonId,Long userId);

    /**
     * 通过邀请码加入教师团队
     *
     * @param code 邀请码
     * @return 是否加入
     */
    Result joinTchTeam(String code);

    /**
     * 判断是否为该课程的教师成员
     *
     * @param lessonId 课程id
     * @param userId   用户id
     * @return null-是的，如果not null，则不是
     */
    Result isLessonTch(Long lessonId, Long userId);
}
