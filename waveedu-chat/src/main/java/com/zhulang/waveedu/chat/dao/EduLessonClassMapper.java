package com.zhulang.waveedu.chat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.chat.po.EduLessonClass;
import com.zhulang.waveedu.chat.query.ClassSimpleInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程班级表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-18
 */
public interface EduLessonClassMapper extends BaseMapper<EduLessonClass> {

    /**
     * 查询学生加入的班级简单信息
     *
     * @param stuId 学生id
     * @return 信息列表
     */
    List<ClassSimpleInfoQuery> selectJoinClassInfoList(@Param("stuId") Long stuId);
}
