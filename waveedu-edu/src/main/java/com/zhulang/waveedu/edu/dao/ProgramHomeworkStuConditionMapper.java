package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.ProgramHomeworkStuCondition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.programhomeworkquery.HomeworkStuCompleteInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 编程作业的学生情况表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-16
 */
public interface ProgramHomeworkStuConditionMapper extends BaseMapper<ProgramHomeworkStuCondition> {

    /**
     * 添加学生信息到作业情况表
     *
     * @param homeworkId 作业id
     * @param classId 班级id
     */
    void insertStuInfoList(@Param("homeworkId") Integer homeworkId, @Param("classId") Long classId);

    /**
     * 查询该作业的所有学生的完成情况
     *
     * @param homeworkId 作业id
     * @return 完成情况
     */
    List<HomeworkStuCompleteInfoQuery> selectHomeworkStuCompleteInfoList(@Param("homeworkId") Integer homeworkId);
}
