package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.vo.LessonFileSimpleInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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


    /**
     * 获取简单的课程文件信息，主要用于在课程主页展示
     *
     * @param lessonId   课程id
     * @param fileId     文件id
     * @param queryLimit 查询最大条数
     * @return 文件id，文件名，文件类型，文件格式，文件格式大小，上传时间
     */
    List<LessonFileSimpleInfoVO> selectSimpleInfoList(@Param("lessonId") Long lessonId,
                                                      @Param("fileId") Long fileId,
                                                      @Param("queryLimit") Integer queryLimit);
}
