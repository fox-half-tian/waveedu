package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonClassCommonHomework;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.homeworkvo.ModifyCommonHomeworkVo;
import com.zhulang.waveedu.edu.vo.homeworkvo.PublishCommonHomeworkVO;
import com.zhulang.waveedu.edu.vo.homeworkvo.SaveCommonHomeworkVO;

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

    /**
     * 发布作业
     *
     * @param publishCommonHomeworkVO 作业id+是否定时发布+定时发布时间
     * @return 发布状况
     */
    Result publish(PublishCommonHomeworkVO publishCommonHomeworkVO);

    /**
     * 修改普通作业信息
     *
     * @param modifyCommonHomeworkVo 作业id，作业标题，难度，截止时间
     * @return 修改状况
     */
    Result modifyInfo(ModifyCommonHomeworkVo modifyCommonHomeworkVo);

    /**
     * 取消预发布，状态变为未发布
     *
     * @param homeworkId 作业Id
     * @return 修改状况
     */
    Result modifyCancelPreparePublish(Integer homeworkId);

    /**
     * 获取班级作业的详细信息，班级创建者可以调用这个接口
     * @param classId 班级id
     * @param isPublish 发布状态
     * @return 作业信息，按照时间从近到远进行了排序
     */
    Result getTchHomeworkDetailListInfo(Long classId, Integer isPublish);

    /**
     * 创建者获取班级作业的简单信息，班级创建者可以调用这个接口
     * 可传 isPublish
     *
     * @param classId 班级id
     * @param isPublish 发布状态
     * @return 作业信息，按照时间从近到远进行了排序
     */
    Result getTchHomeworkSimpleListInfo(Long classId, Integer isPublish);

    /**
     * 校验该 userId 是否为作业的创建者
     *
     * @param id 作业id
     * @param userId 用户id
     * @return true-说明是，false-不是
     */
    boolean existsByIdAndUserId(Integer id, Long userId);
}
