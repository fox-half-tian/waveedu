package com.zhulang.waveedu.edu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.po.Lesson;
import com.zhulang.waveedu.edu.query.LessonBasicInfoQuery;
import com.zhulang.waveedu.edu.query.TchInviteCodeQuery;
import org.apache.ibatis.annotations.Param;

/**
 * 课程表 Mapper 接口
 *
 * @author 狐狸半面添
 * @create 2023-02-03 16:08
 */
public interface LessonMapper extends BaseMapper<Lesson> {

    /**
     * 查询课程基本信息：课程 id，课程名，课程封面，创建时间，创建人id，创建人头像
     *
     * @param lessonId 课程id
     * @return 课程基本情况
     */
    LessonBasicInfoQuery selectBasicInfo(@Param("lessonId") Long lessonId);


    /**
     * 通过 课程id 获取教师邀请码
     *
     * @param id 课程id
     * @return 教师邀请码与是否禁用
     */
    TchInviteCodeQuery selectTchInviteCodeById(@Param("id") Long id);
}
