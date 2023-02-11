package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程学习资料表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-11
 */
public interface LessonFileMapper extends BaseMapper<LessonFile> {

    /**
     * 根据id获取课程id
     *
     * @param id 课程资料id
     * @return 课程id
     */
    Long selectLessonIdById(@Param("id") Long id);
}
