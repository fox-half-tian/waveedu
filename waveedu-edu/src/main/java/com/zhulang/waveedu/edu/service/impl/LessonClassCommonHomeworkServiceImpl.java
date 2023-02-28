package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import java.util.Map;

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
        if (!lessonClassService.existsByUserIdAndClassId(userId, saveCommonHomeworkVO.getLessonClassId())) {
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

    @Override
    public Result publish(Integer commonHomeworkId) {
        // 1.校验格式
        if (commonHomeworkId < 0) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "作业Id格式错误");
        }
        // 2.校验创建者与发布状况
        Map<String, Object> map = this.getMap(new LambdaQueryWrapper<LessonClassCommonHomework>()
                .eq(LessonClassCommonHomework::getId, commonHomeworkId)
                .select(LessonClassCommonHomework::getIsPublish, LessonClassCommonHomework::getCreatorId));
        // 2.1 是否为创建者
        if (!map.get("creator_id").toString().equals(UserHolderUtils.getUserId().toString())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.2 是否是未发布状态
        if ((Integer) map.get("is_publish") != 0) {
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "作业已发布，请勿重复操作");
        }
        // 3.发布，将命令传给 rabbitmq

        return null;
    }
}
