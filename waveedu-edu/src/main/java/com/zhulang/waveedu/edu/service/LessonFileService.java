package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.lessonfilevo.SaveLessonFileVO;

/**
 * <p>
 * 课程学习资料表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-11
 */
public interface LessonFileService extends IService<LessonFile> {
    /**
     * 上传/保存 课程资料
     *
     * @param saveLessonFileVO 课程id + 文件名 + 文件信息 + 用户id
     * @return 结果
     */
    Result saveFile(SaveLessonFileVO saveLessonFileVO);

    /**
     * 删除课程的资料
     *
     * @param lessonFileId 课程资料id
     * @return 删除状况
     */
    Result removeFile(Long lessonFileId);

    /**
     * 修改课程文件的文件名
     *
     * @param fileId   课程文件id
     * @param fileName 新的课程文件名
     * @return 修改状况
     */
    Result modifyFileName(Long fileId, String fileName);

    /**
     * 获取简单的课程文件信息，主要用于在课程主页展示
     *
     * @param lessonId 课程id
     * @param fileId   文件id
     * @return 文件列表信息：文件id + 文件名 + 文件类型 + 文件大小 + 上传的时间，按照时间由近到远排序
     */
    Result getSimpleInfoList(Long lessonId, Long fileId);

    /**
     * 获取详细的课程文件信息
     *
     * @param lessonId 课程id
     * @param fileId   文件id
     * @return 文件列表信息：文件id + 文件名 + 文件类型 + 文件格式 + 文件大小 + 上传的时间 + 上传者id与名字 + 文件路径 + 下载次数，按照时间由近到远排序
     */
    Result getDetailInfoList(Long lessonId, Long fileId);
}
