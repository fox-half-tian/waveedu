package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonClassCommonHomework;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.homework.SaveCommonHomeworkVO;

/**
 * <p>
 * 课程班级的普通作业表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
public interface LessonClassCommonHomeworkService extends IService<LessonClassCommonHomework> {

    /**
     * 保存一份作业
     *
     * @param saveCommonHomeworkVO 作业信息
     * @return 作业id
     */
    Result saveHomework(SaveCommonHomeworkVO saveCommonHomeworkVO);
}
