package com.zhulang.waveedu.edu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.Lesson;
import com.zhulang.waveedu.edu.query.TchInviteCodeQuery;
import com.zhulang.waveedu.edu.vo.SaveLessonVO;

/**
 * 课程表 服务类
 *
 * @author 狐狸半面添
 * @create 2023-02-03 16:09
 */
public interface LessonService extends IService<Lesson> {

    /**
     * 创建课程
     *
     * @param saveLessonVO 课程信息
     * @return 情况
     */
    Result save(SaveLessonVO saveLessonVO);

    /**
     * 获取课程基本信息
     *
     * @param lessonId 课程id
     * @return 基本信息
     */
    Result getBasicInfo(Long lessonId);

    /**
     * 获取教师邀请码
     *
     * @param lessonId 课程id
     * @return 邀请码
     */
    Result getTchInviteCode(Long lessonId);

    /**
     * 通过课程id直接获取邀请码信息
     *
     * @param lessonId 课程id
     * @return 邀请码信息
     */
    TchInviteCodeQuery getTchInviteCodeById(Long lessonId);

    /**
     * 修改教师邀请码
     *
     * @param lessonId 课程id
     * @return 修改后的加密邀请码
     */
    Result modifyTchInviteCode(Long lessonId);
}
