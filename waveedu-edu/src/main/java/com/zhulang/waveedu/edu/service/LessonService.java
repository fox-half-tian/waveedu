package com.zhulang.waveedu.edu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.Lesson;
import com.zhulang.waveedu.edu.query.LessonCacheQuery;
import com.zhulang.waveedu.edu.query.TchInviteCodeQuery;
import com.zhulang.waveedu.edu.vo.ModifyLessonBasicInfoVO;
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

    /**
     * 启用/禁用教师邀请码
     *
     * @param lessonId 课程id
     * @return 是否启用
     */
    Result switchTchInviteCode(Long lessonId);

    /**
     * 通过课程id获取创建者id
     *
     * @param id 课程id
     * @return 创建者id
     */
    Long getCreatorIdByLessonId(Long id);

    /**
     * 删除课程
     *
     * @param lessonId 课程id
     * @return 状况
     */
    Result removeLesson(Long lessonId);

    /**
     * 获取用户创建的所有课程的简单信息
     *
     * @return 信息列表
     */
    Result getCreateLessonSimpleInfoList();

    /**
     * 修改课程的基本信息
     *
     * @param modifyLessonBasicInfoVO 课程id + 课程名 + 课程介绍 + 课程封面
     * @return 修改后的课程基本信息
     */
    Result modifyLessonBasicInfo(ModifyLessonBasicInfoVO modifyLessonBasicInfoVO);

    /**
     * 查询缓存到redis的lesson信息：课程id + 课程名 + 课程介绍 + 课程封面 + 创建人id + 创建时间
     *
     * @param lessonId lesson主键
     * @return 信息
     */
    LessonCacheQuery getNeedCacheInfo(Long lessonId);
}
