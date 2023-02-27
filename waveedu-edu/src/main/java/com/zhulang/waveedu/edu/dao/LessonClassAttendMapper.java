package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClassAttend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.ClassWeekPlanQuery;
import com.zhulang.waveedu.edu.query.EveryTImeStuPlanQuery;
import com.zhulang.waveedu.edu.query.EveryTimeTchPlanQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程班级的上课时间表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
public interface LessonClassAttendMapper extends BaseMapper<LessonClassAttend> {

    /**
     * 通过 id 获取课程班级id
     *
     * @param id 主键
     * @return 班级id
     */
    Long selectLessonClassIdById(@Param("id") Long id);

    /**
     * 获取该班级的上课时间安排
     *
     * @param classId 班级id
     * @return 安排
     */
    List<ClassWeekPlanQuery> selectClassPlan(@Param("classId") Long classId);

    /**
     * 查询教师上课安排
     *
     * @param creatorId 用户id
     * @return 安排
     */
    List<EveryTimeTchPlanQuery> selectTchPlan(@Param("creatorId") Long creatorId);

    /**
     * 查询自己的课程安排
     *
     * @param stuId 学生id
     * @return 安排
     */
    List<EveryTImeStuPlanQuery> selectStuPlan(@Param("stuId") Long stuId);
}
