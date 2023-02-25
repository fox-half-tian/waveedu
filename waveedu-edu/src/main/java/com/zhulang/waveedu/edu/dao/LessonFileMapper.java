package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.LessonFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.LessonFileDetailInfoQuery;
import com.zhulang.waveedu.edu.query.LessonFileSimpleInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
    List<LessonFileSimpleInfoQuery> selectSimpleInfoList(@Param("lessonId") Long lessonId,
                                                         @Param("fileId") Long fileId,
                                                         @Param("queryLimit") Integer queryLimit);

    /**
     * 获取详细的课程文件信息
     *
     * @param lessonId   课程id
     * @param fileId     文件id
     * @param queryLimit 查询最大条数
     * @return 文件列表信息：文件id + 文件名 + 文件类型 + 文件格式 + 文件大小 + 上传的时间 + 上传者id与名字 + 下载次数，按照时间由近到远排序
     */
    List<LessonFileDetailInfoQuery> selectDetailInfoList(@Param("lessonId") Long lessonId,
                                                         @Param("fileId") Long fileId,
                                                         @Param("queryLimit") Integer queryLimit);

    /**
     * 增加一次下载次数
     *
     * @param id 课程文件id
     */
    void updateDownloadCountOfInsertOne(@Param("id") Long id);

    /**
     * 查询该文件的下载次数
     *
     * @param id 课程文件id
     * @return 下载次数
     */
    Integer selectDownloadCount(@Param("id") Long id);

    /**
     * 根据 文件id 查询 文件路径 和 下载次数
     *
     * @param id 文件id
     * @return 文件路径和下载次数
     */
    Map<String,Object> selectFilePathAndDownLoadCount(@Param("id") Long id);
}
