package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonClassProgramHomework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 编程作业表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
public interface LessonClassProgramHomeworkMapper extends BaseMapper<LessonClassProgramHomework> {

    /**
     * 作业状态
     *
     * @param homeworkId 作业id
     * @param creatorId 创建者id
     * @return 0-未发布，1-已发布，2-发布中
     */
    Integer selectIsPublishByHomeworkIdAndCreatorId(@Param("homeworkId") Long homeworkId, @Param("creatorId") Long creatorId);
}
