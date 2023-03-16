package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonClassProgramHomework;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.query.programhomeworkquery.HomeworkIsPublishAndEndTimeAndHomeworkIdQuery;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.ModifyProgramHomeworkVO;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.PublishProgramHomeworkVO;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SaveProgramHomeworkVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 编程作业表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
public interface LessonClassProgramHomeworkService extends IService<LessonClassProgramHomework> {

    /**
     * 添加一份作业
     *
     * @param saveProgramHomeworkVO 班级id + 作业标题
     * @return 作业id
     */
    Result saveHomework(SaveProgramHomeworkVO saveProgramHomeworkVO);

    /**
     * 修改作业信息（标题与截止时间）
     *
     * @param modifyProgramHomeworkVO 标题，截止时间
     * @return 修改状况
     */
    Result modifyInfo(ModifyProgramHomeworkVO modifyProgramHomeworkVO);

    /**
     * 作业状态
     *
     * @param homeworkId 作业id
     * @param creatorId 创建者id
     * @return 0-未发布，1-已发布，2-发布中
     */
    Integer getIsPublishByHomeworkIdAndCreatorId(Integer homeworkId, Long creatorId);

    /**
     * 设置正确的题目数量
     *
     * @param homeworkId 作业id
     */
    void updateNumById(Integer homeworkId);

    /**
     * 删除作业
     *
     * @param homeworkId 作业Id
     * @return 删除状态
     */
    Result removeHomework(Integer homeworkId);

    /**
     * 判断是否为作业的创建者
     *
     * @param homeworkId 作业id
     * @param creatorId 创建者id
     * @return true-是创建者，false-不是
     */
    boolean existsByHomeworkIdAndCreatorId(Integer homeworkId,Long creatorId);

    /**
     * 作业创建者获取班级作业信息列表
     *
     * @param classId 班级id
     * @param status 作业状态：null-所有，0-未发布，1-已发布，2-发布中，3-已截止
     * @return 信息列表，按照时间从近到远排序
     */
    Result tchGetHomeworkInfoList(Long classId, Integer status);

    /**
     * 作业创建者获取作业详细信息
     *
     * @param homeworkId 作业id
     * @return 详细信息
     */
    Result tchGetHomeworkDetailInfo(Integer homeworkId);

    /**
     * 发布作业
     *
     * @param publishProgramHomeworkVO 作业id+是否定时发布+定时发布时间
     * @return 发布状况
     */
    Result publish(PublishProgramHomeworkVO publishProgramHomeworkVO);

    /**
     * 根据问题id查询作业的发布状况与截止时间
     *
     * @param problemId 问题id
     * @return 信息
     */
    HomeworkIsPublishAndEndTimeAndHomeworkIdQuery getIsPublishAndEndTimeAndHomeworkIdByProblemId(Integer problemId);

    /**
     * 学生获取班级作业的简单信息列表
     *
     * @param classId 班级id
     * @return 信息列表
     */
    Result stuGetHomeworkSimpleListInfo(Long classId);

    /**
     * 学生获取某个作业的详细信息
     *
     * @param homeworkId 作业id
     * @return 详细信息
     */
    Result stuGetHomeworkDetailInfo(Integer homeworkId);

    /**
     * 根据 作业id 查询数量
     *
     * @param id 作业id
     * @return 数量
     */
    Integer getNumById(Integer id);


}
