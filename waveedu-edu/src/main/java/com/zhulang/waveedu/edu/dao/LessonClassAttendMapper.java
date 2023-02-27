package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClassAttend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
}
