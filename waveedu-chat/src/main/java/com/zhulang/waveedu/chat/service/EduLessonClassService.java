package com.zhulang.waveedu.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.chat.po.EduLessonClass;
import com.zhulang.waveedu.common.entity.Result;

/**
 * <p>
 * 课程班级表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-18
 */
public interface EduLessonClassService extends IService<EduLessonClass> {

    /**
     * 获取所有管理与加入的班级列表
     *
     * @return 列表
     */
    Result getClassInfoList();
}
