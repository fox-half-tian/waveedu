package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClassStu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.ClassStuInfoQuery;
import com.zhulang.waveedu.edu.query.JoinClassInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程班级与学生的对应关系表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-17
 */
public interface LessonClassStuMapper extends BaseMapper<LessonClassStu> {


    /**
     * 查询加入的班级信息列表，
     * 按照加入时间由近及远排序
     *
     * @param userId 用户id
     * @return 班级id + 班级名 + 是否结课 + 课程id + 课程名 + 课程封面
     */
    List<JoinClassInfoQuery> selectJoinClassInfoList(@Param("userId") Long userId);

    /**
     * 查询该用户是否为课程班级的普通成员
     *
     * @param lessonId 课程id
     * @param userId   用户id
     * @return null-说明不是
     */
    Integer existsByLessonIdAndUserId(@Param("lessonId") Long lessonId, @Param("userId") Long userId);

    /**
     * 查询该用户是否为该班级的普通成员
     *
     * @param classId 班级Id
     * @param userId  用户id
     * @return null-说明不是
     */
    Integer existsByClassIdAndUserId(@Param("classId") Long classId, @Param("userId") Long userId);

    /**
     * 查询班级中的所有学生信息：用户id，用户名，用户头像，学号，身份类型，院校名
     *
     * @param classId 班级id
     * @return 学生信息列表
     */
    List<ClassStuInfoQuery> selectStuInfoList(@Param("classId") Long classId);


}
