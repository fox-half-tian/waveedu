package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClassStu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
