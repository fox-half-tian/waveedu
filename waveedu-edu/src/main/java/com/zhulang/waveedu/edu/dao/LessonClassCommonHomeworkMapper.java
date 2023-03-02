package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClassCommonHomework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.TchHomeworkSimpleInfoQuery;
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
    List<TchHomeworkSimpleInfoQuery> getTchHomeworkSimpleListInfo(@Param("classId") Long classId,
                                                                  @Param("isPublish") Integer isPublish);
}
