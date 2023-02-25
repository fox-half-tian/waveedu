package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonClass;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.query.LessonClassInviteCodeQuery;
import com.zhulang.waveedu.edu.vo.classvo.ModifyClassBasicInfoVO;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassVO;

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
     * 通过 用户id 和 班级id 判断是否存在该信息 --> 是否为创始人
     *
     * @param userId 用户id
     * @param classId 班级id
     * @return true-该用户是创建者
     */
    boolean existsByUserIdAndClassId(Long userId, Long classId);


}
