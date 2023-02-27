package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.ClassBasicInfoQuery;
import com.zhulang.waveedu.edu.query.CreateLessonClassInfoQuery;
import com.zhulang.waveedu.edu.query.LessonClassInfoQuery;
import com.zhulang.waveedu.edu.query.LessonClassInviteCodeQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * 判断该班级的创建者并且班级未被删除
     *
     * @param id 班级id
     * @param userId 用户id
     */
    Integer existsByClassIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 判断该班级的创建者，不管班级是否已被删除
     *
     * @param id 班级id
     * @param userId 用户id
     */
    Integer isCreatorByUserIdOfClassId(@Param("id") Long id, @Param("userId") Long userId);

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
    LessonClassInviteCodeQuery selectInviteCodeById(@Param("id") Long id);

    /**
     * 动态修改人数
     *
     * @param classId 班级id
     * @param change 动态增减
     */
    void updateNumOfDynamic(@Param("classId") Long classId,@Param("change") String change);

    /**
     * 获取创建的班级信息列表,按照时间由近到远排序
     *
     * @param creatorId 创建者id
     * @param isEndClass 是否结课
     * @param classId 班级id
     * @param limitQuery 最大查询数量
     * @return 信息列表：班级id,班级名，班级人数,课程封面，课程名，课程id
     */
    List<CreateLessonClassInfoQuery> selectCreateClassInfoList(@Param("creatorId") Long creatorId,
                                                               @Param("isEndClass") Integer isEndClass,
                                                               @Param("classId") Long classId,
                                                               @Param("limitQuery") Integer limitQuery);

    /**
     * 查询课程的所有班级
     *
     * @param lessonId 课程id
     * @return 班级信息：班级id，班级名，学生人数，是否结课，是否禁止加入，创建时间，创建者姓名，邀请码
     */
    List<LessonClassInfoQuery> selectLessonAllClassInfoList(@Param("lessonId") Long lessonId);

}
