package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonClassAttend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassAttendVO;

/**
 * <p>
 * 课程班级的上课时间表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
public interface LessonClassAttendService extends IService<LessonClassAttend> {

    /**
     * 保存一份上课时间信息
     *
     * @param saveClassAttendVO 班级id,星期，时间，课程名
     * @return 信息id
     */
    Result saveOne(SaveClassAttendVO saveClassAttendVO);

    /**
     * 删除一份上课时间信息
     *
     * @param attendId 信息id
     * @return 删除状况
     */
    Result delOne(Long attendId);
}
