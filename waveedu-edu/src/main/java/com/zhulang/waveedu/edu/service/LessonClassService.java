package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.LessonClass;
import com.baomidou.mybatisplus.extension.service.IService;
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

}
