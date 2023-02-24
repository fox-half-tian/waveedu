package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    Integer isCreatorByUserIdOfId(@Param("id") Long id, @Param("userId") Long userId);
}
