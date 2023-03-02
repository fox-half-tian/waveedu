package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonTch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.lessonquery.LessonTchInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程与教学团队的对应表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-03
 */
public interface LessonTchMapper extends BaseMapper<LessonTch> {

    /**
     * 通过 lessonId 和 userId 查询该用户是否为该课程的教师
     *
     * @param lessonId 课程id
     * @param userId 用户id
     * @return null 表示不存在，非空表示存在
     */
    Integer isExistByLessonAndUser(@Param("lessonId") Long lessonId,@Param("userId") Long userId);

    /**
     * 获取某个课程的教学团队
     *
     * @param lessonId 课程id
     * @return 教学团队信息：用户id + 用户名 + 用户头像 + 用户所在单位单位名
     */
    List<LessonTchInfoQuery> selectTchTeamInfo(@Param("lessonId")Long lessonId);
}
