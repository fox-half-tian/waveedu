package com.zhulang.waveedu.edu.service;

import com.alibaba.fastjson.JSON;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.edu.po.LessonFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.SaveLessonFileVO;
import org.springframework.beans.BeanUtils;

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
     * @param lessonId 课程id
     * @param lessonFileId  课程资料id
     * @return 删除状况
     */
    Result removeFile(Long lessonId,Long lessonFileId);
}
