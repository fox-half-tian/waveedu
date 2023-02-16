package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonSectionFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.SaveSectionFileVO;

/**
 * <p>
 * 课程小节的文件表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-16
 */
public interface LessonSectionFileService extends IService<LessonSectionFile> {

    /**
     * 保存小节的视频资料
     *
     * @param saveSectionFileVO 用户id + 小节id + 文件名  + 文件加密信息
     * @return 保存结果
     */
    Result saveVideoFile(SaveSectionFileVO saveSectionFileVO);
}
