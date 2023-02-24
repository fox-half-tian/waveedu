package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.ClassBasicInfoQuery;
import com.zhulang.waveedu.edu.query.LessonClassInviteCodeQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程班级表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-17
 */
public interface LessonClassMapper extends BaseMapper<LessonClass> {

    /**
     * 判断是否为该班级的创建者
     *
     * @param id 班级id
     * @param userId 用户id
     */
    Integer existByUserIdAndClassId(@Param("id") Long id, @Param("userId") Long userId);


    /**
     * 查询班级的基本信息：创建者id，班级名，封面，人数，是否结课，课程id，创建时间
     *
     * @param classId 课程id
     * @return 班级基本信息
     */
    ClassBasicInfoQuery selectBasicInfo(Long classId);

    /**
     * 查询邀请码与是否禁止加入
     *
     * @param id 班级id
     * @return 信息
     */
    LessonClassInviteCodeQuery getInviteCodeById(@Param("id") Long id);
}
