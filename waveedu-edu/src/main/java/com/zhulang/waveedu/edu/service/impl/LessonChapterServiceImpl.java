package com.zhulang.waveedu.edu.service.impl;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import com.zhulang.waveedu.edu.po.LessonChapter;
import com.zhulang.waveedu.edu.dao.LessonChapterMapper;
import com.zhulang.waveedu.edu.service.LessonChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonTchService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 课程章节表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-15
 */
@Service
public class LessonChapterServiceImpl extends ServiceImpl<LessonChapterMapper, LessonChapter> implements LessonChapterService {
    @Resource
    private LessonChapterMapper lessonChapterMapper;
    @Resource
    private LessonTchService lessonTchService;

    @Override
    public Result saveChapter(Long lessonId, String name) {
        Long userId = UserHolderUtils.getUserId();
        // 1.校验是否为教学团队成员
        Result result = lessonTchService.isLessonTch(lessonId, userId);
        if (result != null) {
            return result;
        }
        // 2.获取课程的最新章节
        Integer maxOrderBy = lessonChapterMapper.getMaxOrderByOfLessonId(lessonId);
        // 3.设置当前章节的序号
        int orderBy;
        if (maxOrderBy == null){
            orderBy = 1;
        }else{
            orderBy = maxOrderBy + 1;
        }
        // 4.封装信息
        LessonChapter lessonChapter = new LessonChapter();
        // 4.1 设置课程id
        lessonChapter.setLessonId(lessonId);
        // 4.2 设置课程的章节名
        lessonChapter.setName(WaveStrUtils.removeBlank(name));
        // 4.3 设置顺序
        lessonChapter.setOrderBy(orderBy);
        // 4.4 设置创建者为当前用户
        lessonChapter.setCreatorId(userId);

        // 5.保存信息
        lessonChapterMapper.insert(lessonChapter);
        // 6.返回章节的id
        return Result.ok(lessonChapter.getId());
    }
}
