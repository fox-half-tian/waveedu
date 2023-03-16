package com.zhulang.waveedu.messagesdk.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.messagesdk.po.ProgramHomeworkStuCondition;
import org.apache.ibatis.annotations.Param;

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
}
