package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClassCommonHomework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.commonhomeworkquery.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程班级的普通作业表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
public interface LessonClassCommonHomeworkMapper extends BaseMapper<LessonClassCommonHomework> {

    /**
     * 查询 班级创建者/课程创建者 的id
     *
     * @param id 普通作业id
     * @return 创建者id
     */
    Long selectCreatorIdById(Integer id);

    /**
     * 查询作业的简单信息列表
     *
     * @param classId 班级id
     * @param isPublish 发布状况
     * @return 信息列表：id,title，作业状态
     */
    List<TchHomeworkSimpleInfoQuery> selectTchHomeworkSimpleListInfo(@Param("classId") Long classId,
                                                                     @Param("isPublish") Integer isPublish);

    /**
     * 校验该 userId 是否为作业的创建者
     *
     * @param id 作业id
     * @param creatorId 用户id
     * @return 非null-是，Null-不是
     */
    Integer existsByIdAndCreatorId(@Param("id") Integer id, @Param("creatorId") Long creatorId);

    /**
     * 教师身份查询作业的详细信息列表
     *
     * @param id 作业id
     * @return 作业表全部信息 + 班级人数
     */
    TchHomeworkDetailInfoQuery selectTchHomeworkDetailInfo(@Param("id")Integer id);

    /**
     * 修改作业表的总分数，根据问题表的题目累加得到总分
     *
     * @param id 作业Id
     */
    void updateTotalScore(@Param("id") Integer id);


    /**
     * 学生查询对班级作业的简单信息列表
     *
     * @param classId 班级id
     * @param stuId 学生id
     * @return 信息列表
     */
    List<StuHomeworkSimpleInfoQuery> selectStuHomeworkSimpleInfoList(@Param("classId") Long classId, @Param("stuId") Long stuId);

    /**
     * 通过作业id和用户id判断用户是否该作业所在班级的学生
     *
     * @param id 作业id
     * @param stuId 用户id
     * @return null-说明不是，非null-说明是
     */
    Integer isClassStuByIdAndStuId(@Param("id") Integer id, @Param("stuId") Long stuId);

    /**
     * 学生查询到的一个作业的详细信息：类型，标题，难度，满分，学生分数，开始时间，结束时间，状态
     *
     * @param id 作业id
     * @return 信息
     */
    StuHomeworkDetailInfoQuery selectStuHomeworkDetailInfo(@Param("id") Integer id);

    /**
     * 根据作业id查询作业的状态
     *
     * @param id 作业id
     * @param stuId 学生id
     * @return 作业状态信息
     */
    StuHomeworkStatusQuery selectStuHomeworkStatus(@Param("id") Integer id,@Param("stuId") Long stuId);

    /**
     * 提交人数加一
     *
     * @param id 作业id
     */
    void updateCommitNumOfAddOne(@Param("id") Integer id);
}
