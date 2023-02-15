package com.zhulang.waveedu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import com.zhulang.waveedu.edu.po.LessonChapter;
import com.zhulang.waveedu.edu.po.LessonSection;
import com.zhulang.waveedu.edu.dao.LessonSectionMapper;
import com.zhulang.waveedu.edu.service.LessonChapterService;
import com.zhulang.waveedu.edu.service.LessonSectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonTchService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 课程章节的小节表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-15
 */
@Service
public class LessonSectionServiceImpl extends ServiceImpl<LessonSectionMapper, LessonSection> implements LessonSectionService {
    @Resource
    private LessonSectionMapper lessonSectionMapper;
    @Resource
    private LessonChapterService lessonChapterService;
    @Resource
    private LessonTchService lessonTchService;

    @Override
    public Result saveSection(Integer chapterId, String name) {
        // 1.判断章节是否存在
        Long lessonId = lessonChapterService.getLessonIdById(chapterId);
        if (lessonId == null) {
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "章节不存在");
        }

        Long userId = UserHolderUtils.getUserId();
        // 2.判断是否为教学团队成员
        Result result = lessonTchService.isLessonTch(lessonId, userId);
        if (result != null) {
            return result;
        }

        // 3.获取最新的小节号
        Integer maxOrderBy = lessonSectionMapper.getMaxOrderByOfChapterId(chapterId);
        // 4.设置当前小节的序号
        int orderBy;
        if (maxOrderBy == null) {
            orderBy = 1;
        } else {
            orderBy = maxOrderBy + 1;
        }
        // 5.封装信息
        LessonSection lessonSection = new LessonSection();
        // 5.1 设置章节id
        lessonSection.setChapterId(chapterId);
        // 5.2 设置章节的小节名
        lessonSection.setName(name.trim());
        // 5.3 设置顺序
        lessonSection.setOrderBy(orderBy);
        // 5.4 设置创建者为当前用户
        lessonSection.setCreatorId(userId);

        // 6.保存信息
        lessonSectionMapper.insert(lessonSection);
        // 7.返回小节的id
        return Result.ok(lessonSection.getId());

    }

    @Override
    public Result removeSection(Integer sectionId) {
        // 1.判断sectionId是否合法
        if (sectionId < 1) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "小节id格式错误");
        }
        // 2.获取该小节的章节id
        Integer chapterId = lessonSectionMapper.selectChapterIdById(sectionId);
        if (chapterId == null) {
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "小节不存在");
        }

        // 3.判断是否为教学团队成员
        Long lessonId = lessonChapterService.getLessonIdById(chapterId);
        if (lessonId == null) {
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "章节不存在");
        }
        Result result = lessonTchService.isLessonTch(lessonId, UserHolderUtils.getUserId());
        if (result != null) {
            return result;
        }

        // 4.删除小节并返回
        return lessonSectionMapper.deleteById(sectionId) != 0 ? Result.ok() : Result.error();
    }
}
