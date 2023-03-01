package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonClass;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.query.LessonClassInviteCodeQuery;
import com.zhulang.waveedu.edu.vo.classvo.ModifyClassBasicInfoVO;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassVO;

import java.util.Map;

/**
 * <p>
 * 课程班级表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-17
 */
public interface LessonClassService extends IService<LessonClass> {

    /**
     * 保存课程班级
     *
     * @param saveClassVO 班级名 + 班级封面 + 课程id
     * @return 班级Id
     */
    Result saveClass(SaveClassVO saveClassVO);

    /**
     * 修改班级基本信息：班级名 , 封面 ，是否结课，是否禁止加入班级
     *
     * @param modifyClassBasicInfoVO name + cover + isEndClass +isForbidJoin + id
     * @return 修改情况
     */
    Result modifyBasicInfo(ModifyClassBasicInfoVO modifyClassBasicInfoVO);

    /**
     * 更换邀请码
     *
     * @param classId 班级id
     * @return 新的邀请码
     */
    Result modifyInviteCode(Long classId);

    /**
     * 获取班级的详细信息，只有创建者可以获取
     *
     * @param classId 班级id
     * @return 详细信息
     */
    Result getDetailInfo(Long classId);

    /**
     * 获取班级基本信息--》都可以查看
     *
     * @param classId 班级id
     * @return 基本信息
     */
    Result getBasicInfo(Long classId);


    /**
     * 根据班级id获取班级邀请码和是否禁止加入班级
     *
     * @param id 班级id
     * @return 信息
     */
    LessonClassInviteCodeQuery getInviteCodeById(Long id);

    /**
     * 通过 用户id 和 班级id 判断是否存在该信息且未被删除 --> 是否为创始人
     *
     * @param userId 用户id
     * @param classId 班级id
     * @return true-该用户是创建者
     */
    boolean existsByUserIdAndClassId(Long userId, Long classId);

    /**
     * 通过 用户id 和 班级id 判断是否存在该信息不管是否被删除 --> 是否为创始人
     *
     * @param userId 用户id
     * @param classId 班级id
     * @return true-该用户是创建者
     */
    boolean isCreatorByUserIdOfClassId(Long userId, Long classId);

    /**
     * 删除自己创建的班级
     *
     * @param classId 班级Id
     * @return 删除状况
     */
    Result delClass(Long classId);

    /**
     * 动态修改人数
     *
     * @param classId 班级id
     * @param change 动态增减
     */
    void modifyNumOfDynamic(Long classId, String change);

    /**
     * 获取创建的班级信息列表
     * 按照时间由近及远排序
     *
     * @param isEndClass 是否结课
     * @param classId 班级id,返回列表信息均小于该id
     * @return 信息列表：班级id,班级名，班级人数,课程封面，课程名，课程id
     */
    Result getCreateClassInfoList(Integer isEndClass,Long classId);

    /**
     * 获取该课程的所有班级
     *
     * @param lessonId 课程id
     * @return 班级信息
     */
    Result getLessonAllClassInfoList(Long lessonId);

    /**
     * 获取该课程中自己创建的所有班级
     * 教学团队成员可以操作
     * 已按照时间由近到远排序
     *
     * @param lessonId 课程id
     * @return 班级信息:班级id，班级名，学生人数，是否结课，是否禁止加入，创建时间，邀请码
     */
    Result getLessonSelfAllClassInfoList(Long lessonId);

    /**
     * 判断是否已经结课
     *
     * @param classId 班级id
     * @return true-说明结课
     */
    boolean isEndClassById(Long classId);

    /**
     * 删除班级以及班级信息
     *
     * @param classId 班级Id
     * @return 删除状况
     */
    Result delClassInfo(Long classId);

    /**
     * 根据班级id 查询班级的创建者以及班级所在课程的课程名
     *
     * @param classId 班级id
     * @return 班级的创建者以及班级所在课程的课程名
     */
    Map<String,Object> getLessonNameAndCreatorIdById(Long classId);
}
