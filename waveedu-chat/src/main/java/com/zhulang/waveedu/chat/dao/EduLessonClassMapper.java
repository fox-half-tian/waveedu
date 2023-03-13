package com.zhulang.waveedu.chat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.chat.pojo.EduLessonClassStu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 阿东
 * @date 2023/3/13 [3:35]
 */
@Mapper
public interface EduLessonClassMapper extends BaseMapper<EduLessonClassStu> {
    /**
     * 通过班级ID和学生ID获取班级信息
     * @param classId 班级ID
     * @param userId 学生ID
     * @return 这个班级是否存在该学生
     */
    EduLessonClassStu  getEduLessonClassStuByClassIdAndUserId(@Param("classId")Long classId,@Param("userId")Long userId);

    /**
     * 返回同一个班级的学生ID
     * @param classId 班级ID
     * @return 返回同一个班级的学生ID
     */
    List<Long> getUserIdByClassId(@Param("classId")Long classId);
}
