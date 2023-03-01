package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClassCommonHomework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
}
