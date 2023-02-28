package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.LessonClassCommonHomework;
import com.zhulang.waveedu.edu.dao.LessonClassCommonHomeworkMapper;
import com.zhulang.waveedu.edu.service.LessonClassCommonHomeworkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonClassService;
import com.zhulang.waveedu.edu.vo.homework.SaveCommonHomeworkVO;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 课程班级的普通作业表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
@Service
public class LessonClassCommonHomeworkServiceImpl extends ServiceImpl<LessonClassCommonHomeworkMapper, LessonClassCommonHomework> implements LessonClassCommonHomeworkService {
    @Resource
    private LessonClassCommonHomeworkMapper lessonClassCommonHomeworkMapper;
    @Resource
    private LessonClassService lessonClassService;

    @Override
    public Result saveHomework(SaveCommonHomeworkVO saveCommonHomeworkVO) {
        Long userId = UserHolderUtils.getUserId();
        // 1.判断是否为班级创建者
        if (!lessonClassService.existsByUserIdAndClassId(userId, saveCommonHomeworkVO.getLessonClassId())){
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.属性转换
        LessonClassCommonHomework homework = BeanUtil.copyProperties(saveCommonHomeworkVO, LessonClassCommonHomework.class);
        // 3.添加创建者
        homework.setCreatorId(userId);
        // 3.插入数据
        lessonClassCommonHomeworkMapper.insert(homework);
        // 5.返回作业id
        return Result.ok(homework.getId());
    }
}
