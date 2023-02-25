package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonClassFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassFileVO;

/**
 * <p>
 * 课程班级资料表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-25
 */
public interface LessonClassFileService extends IService<LessonClassFile> {

    /**
     * 上传/保存 班级资料
     *
     * @param saveClassFileVO 班级id + 文件名 + 文件信息
     * @return 资料信息
     */
    Result saveFile(SaveClassFileVO saveClassFileVO);

    /**
     * 删除班级的资料
     *
     * @param lessonClassFileId 班级资料id
     * @return 删除状况
     */
    Result removeFile(Long lessonClassFileId);

    /**
     * 修改班级文件的文件名
     *
     * @param fileId   课程文件id
     * @param fileName 新的班级文件名
     * @return 修改状况
     */
    Result modifyFileName(Long fileId, String fileName);

    /**
     * 获取班级文件信息
     *
     * @param lessonClassId 班级id
     * @param fileId        文件id
     * @return 文件列表信息：文件id + 文件名 + 文件类型 + 文件格式 + 文件大小 + 上传的时间 + 上传者id与名字 + 下载次数，按照时间由近到远排序
     */
    Result getInfoList(Long lessonClassId, Long fileId);
}
