package com.zhulang.waveedu.edu.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.InviteCodeTypeConstants;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.*;
import com.zhulang.waveedu.edu.constant.EduConstants;
import com.zhulang.waveedu.edu.dao.LessonMapper;
import com.zhulang.waveedu.edu.po.Lesson;
import com.zhulang.waveedu.edu.po.LessonTch;
import com.zhulang.waveedu.edu.query.chapterquery.ChapterNameInfoWithSectionListQuery;
import com.zhulang.waveedu.edu.query.lessonquery.CreateOrTchLessonSimpleInfoQuery;
import com.zhulang.waveedu.edu.query.lessonquery.LessonBasicInfoQuery;
import com.zhulang.waveedu.edu.query.lessonquery.LessonCacheQuery;
import com.zhulang.waveedu.edu.query.lessonquery.TchInviteCodeQuery;
import com.zhulang.waveedu.edu.service.LessonClassStuService;
import com.zhulang.waveedu.edu.service.LessonService;
import com.zhulang.waveedu.edu.service.LessonTchService;
import com.zhulang.waveedu.edu.service.UserInfoService;
import com.zhulang.waveedu.edu.vo.lessonvo.ModifyLessonBasicInfoVO;
import com.zhulang.waveedu.edu.vo.lessonvo.SaveLessonVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程表 服务实现类
 *
 * @author 狐狸半面添
 * @create 2023-02-03 16:07
 */
@Service
public class LessonServiceImpl extends ServiceImpl<LessonMapper, Lesson> implements LessonService {
    @Resource
    private LessonMapper lessonMapper;
    @Resource
    private LessonTchService lessonTchService;
    @Resource
    private RedisCacheUtils redisCacheUtils;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private LessonClassStuService lessonClassStuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result save(SaveLessonVO saveLessonVO) {
        Lesson lesson = new Lesson();
        Long userId = UserHolderUtils.getUserId();
        // 1.将前端传来的信息放入Lesson对象
        // 1.1 课程名
        lesson.setName(WaveStrUtils.removeBlank(saveLessonVO.getName()));
        // 1.2 介绍信息
        lesson.setIntroduce(WaveStrUtils.removeBlank(saveLessonVO.getIntroduce()));
        // 1.3 头像
        lesson.setCover(saveLessonVO.getCover() != null ? saveLessonVO.getCover() : EduConstants.DEFAULT_LESSON_COVER);

        // 2.设置创建者
        lesson.setCreatorId(userId);
        // 3.生成邀请码
        lesson.setTchInviteCode(RandomUtil.randomString(6));
        // 4.加入到 edu_lesson 表中
        lessonMapper.insert(lesson);
        // 5.加入到 edu_lesson_tch 表中
        LessonTch lessonTch = new LessonTch();
        lessonTch.setLessonId(lesson.getId());
        lessonTch.setUserId(userId);
        lessonTchService.save(lessonTch);
        // 6.返回成功
        return Result.ok(lesson.getId());
    }

    @Override
    public Result getBasicInfo(Long lessonId) {
        String lessonInfoKey = RedisConstants.LESSON_INFO_KEY + lessonId;
        // 1.查询redis中是否存在课程信息
        Map<String, Object> cacheLessonMap = redisCacheUtils.getCacheMap(lessonInfoKey);
        // 2.如果不存在，就去数据库中查找
        if (cacheLessonMap == null || cacheLessonMap.size() == 0) {
            // 2.1 查询信息
            LessonBasicInfoQuery info = lessonMapper.selectBasicInfo(lessonId);
            // 2.2 不存在，则直接返回
            if (info == null) {
                return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
            }
            // 2.3 存在，则缓存到redis
            Map<String, Object> map = BeanUtil.beanToMap(info, false, true);
            map.remove("creatorIcon");
            redisCacheUtils.setCacheMap(lessonInfoKey, map);
            redisCacheUtils.expire(lessonInfoKey, RedisConstants.LESSON_INFO_TTL);
            return Result.ok(info);
        }
        // 3.如果存在，则获取，并从数据库获取 用户icon
        LessonBasicInfoQuery info = BeanUtil.fillBeanWithMap(cacheLessonMap, new LessonBasicInfoQuery(), false);
        info.setCreatorIcon(userInfoService.getIconByUserId((Long) cacheLessonMap.get("creatorId")));
        return Result.ok(info);
    }

    @Override
    public Result getTchInviteCode(Long lessonId) {
        // 1.lessonId是否合理
        if (RegexUtils.isSnowIdInvalid(lessonId)) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
        }

        // 2.当前用户是否为教师团队一员，规定只有该课程的教师才可以获取邀请码
        if (!lessonTchService.isExistByLessonAndUser(lessonId, UserHolderUtils.getUserId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }

        // 3.获取邀请码
        TchInviteCodeQuery tchInviteCodeQuery = lessonMapper.selectTchInviteCodeById(lessonId);

        // 4.获取不到说明不存在 -> 返回前端
        if (tchInviteCodeQuery == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
        }

        // 5.邀请码被禁用 -> 返回前端
        if (tchInviteCodeQuery.getCodeIsForbidden() == 1) {
            return Result.error(HttpStatus.HTTP_INFO_REFUSE.getCode(), "邀请码已被禁用");
        }

        // 6.对邀请码进行处理
        String encryptCode = CipherUtils.encrypt(InviteCodeTypeConstants.LESSON_TCH_TEAM_CODE_TYPE + "-" + lessonId + "-" + tchInviteCodeQuery.getTchInviteCode());

        // 6.获取邀请码，成功返回
        return Result.ok(encryptCode);
    }

    @Override
    public TchInviteCodeQuery getTchInviteCodeById(Long lessonId) {
        return lessonMapper.selectTchInviteCodeById(lessonId);
    }

    @Override
    public Result modifyTchInviteCode(Long lessonId) {
        // 1.lessonId是否合理
        if (RegexUtils.isSnowIdInvalid(lessonId)) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
        }

        // 2.当前用户是否为教师团队一员，规定只有该课程的教师才可以修改邀请码
        if (!lessonTchService.isExistByLessonAndUser(lessonId, UserHolderUtils.getUserId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }

        // 4.查询原邀请码信息
        TchInviteCodeQuery codeInfo = lessonMapper.selectTchInviteCodeById(lessonId);
        // 5.为空 -> 说明课程已被删除
        if (codeInfo == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
        }
        // 6.查询是否被禁用 -> 被禁用，返回前端
        if (codeInfo.getCodeIsForbidden() == 1) {
            return Result.error(HttpStatus.HTTP_INFO_REFUSE.getCode(), "邀请码已被禁用");
        }
        // 7.生成新的真实邀请码
        String code = RandomUtil.randomString(6);
        while (code.equals(codeInfo.getTchInviteCode())) {
            code = RandomUtil.randomString(6);
        }
        // 8.修改数据库信息
        LambdaUpdateWrapper<Lesson> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Lesson::getTchInviteCode, code)
                .eq(Lesson::getId, lessonId);
        this.update(wrapper);
        // 9.返回给前端加密后的邀请码
        return Result.ok(CipherUtils.encrypt(InviteCodeTypeConstants.LESSON_TCH_TEAM_CODE_TYPE + "-" + lessonId + "-" + code));
    }

    @Override
    public Result switchTchInviteCode(Long lessonId) {
        // 1.lessonId是否合理
        if (RegexUtils.isSnowIdInvalid(lessonId)) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
        }
        // 2.当前用户是否为教师团队一员，规定只有该课程的教师才可以启用/禁用邀请码
        if (!lessonTchService.isExistByLessonAndUser(lessonId, UserHolderUtils.getUserId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 4.查询邀请码信息
        TchInviteCodeQuery codeInfo = lessonMapper.selectTchInviteCodeById(lessonId);
        // 5.为空 -> 说明课程已被删除
        if (codeInfo == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
        }
        // 6.当前情况
        LambdaUpdateWrapper<Lesson> wrapper = new LambdaUpdateWrapper<>();
        if (codeInfo.getCodeIsForbidden() == 0) {
            // 6.1 说明已启用 -> 修改为禁用
            wrapper.set(Lesson::getCodeIsForbidden, 1)
                    .eq(Lesson::getId, lessonId);
            this.update(wrapper);
            // 返回
            HashMap<String, String> map = new HashMap<>(1);
            map.put("status", "off");
            return Result.ok(map);
        } else {
            // 6.2 说明已禁用 -> 修改为启用
            wrapper.set(Lesson::getCodeIsForbidden, 0)
                    .eq(Lesson::getId, lessonId);
            this.update(wrapper);
            // 返回
            HashMap<String, String> map = new HashMap<>(2);
            map.put("status", "on");
            map.put("tchInviteCode", CipherUtils.encrypt(lessonId + "-" + codeInfo.getTchInviteCode()));
            return Result.ok(map);
        }

    }

    @Override
    public Long getCreatorIdByLessonId(Long id) {
        return lessonMapper.selectCreatorIdById(id);
    }

    @Override
    public Result removeLesson(Long lessonId) {
        // 1.校验id
        if (RegexUtils.isSnowIdInvalid(lessonId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效课程id");
        }

        String lessonInfoKey = RedisConstants.LESSON_INFO_KEY + lessonId;
        // 2.从缓存中获取课程的创建者
        Long creatorId = redisCacheUtils.getCacheMapValue(lessonInfoKey, "creatorId");

        // 3.如果不存在，则从数据库中获取
        if (creatorId == null) {
            // 3.1 获取课程的创建者
            creatorId = lessonMapper.selectCreatorIdById(lessonId);
            // 3.2 判断课程是否存在
            if (creatorId == null) {
                return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "课程不存在");
            }
        }
        // 4.判断是否为课程创建者
        if (creatorId.longValue() != UserHolderUtils.getUserId().longValue()) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }

        // 5.逻辑删除课程
        lessonMapper.deleteById(lessonId);
        // 6.从缓存中移除
        redisCacheUtils.deleteObject(RedisConstants.LESSON_INFO_KEY + lessonId);
        // 7.返回
        return Result.ok();
    }

    @Override
    public Result getCreateLessonSimpleInfoList() {
        List<CreateOrTchLessonSimpleInfoQuery> infoList = lessonMapper.selectCreateLessonSimpleInfoList(UserHolderUtils.getUserId());
        return Result.ok(infoList);
    }

    @Override
    public Result modifyLessonBasicInfo(ModifyLessonBasicInfoVO modifyLessonBasicInfoVO) {
        // 0.校验课程名
        String name = modifyLessonBasicInfoVO.getName();
        if (name != null) {
            name = WaveStrUtils.removeBlank(name);
            if (!StringUtils.hasText(name)) {
                return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "课程名格式错误");
            }
            modifyLessonBasicInfoVO.setName(name);
        }
        // 1.判断是否为教学团队成员
        Result result = lessonTchService.isLessonTch(modifyLessonBasicInfoVO.getId(), UserHolderUtils.getUserId());
        if (result != null) {
            return result;
        }
        // 2.修改信息
        Lesson lesson = BeanUtil.copyProperties(modifyLessonBasicInfoVO, Lesson.class);
        lessonMapper.updateById(lesson);
        // 3.获取修改后的新信息
        LessonBasicInfoQuery info = lessonMapper.selectBasicInfo(lesson.getId());
        // 4.修改缓存信息
        HashMap<String, Object> map = new HashMap<>();
        if (modifyLessonBasicInfoVO.getName() != null) {
            map.put("name", info.getName());
        }
        if (modifyLessonBasicInfoVO.getIntroduce() != null) {
            map.put("introduce", info.getIntroduce());
        }
        if (modifyLessonBasicInfoVO.getCover() != null) {
            map.put("cover", info.getCover());
        }
        redisCacheUtils.setCacheMap(RedisConstants.LESSON_INFO_KEY + info.getId(), map);
        return Result.ok(info);
    }

    @Override
    public LessonCacheQuery getNeedCacheInfo(Long lessonId) {
        return lessonMapper.selectNeedCacheInfo(lessonId);
    }

    @Override
    public Result getIdentity(Long lessonId) {
        // 1.校验lessonId
        if (RegexUtils.isSnowIdInvalid(lessonId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "课程id格式错误");
        }

        Long userId = UserHolderUtils.getUserId();

        // 2.查询是否为课程创建者
        // 2.1 查询课程创建者的id
        Long creatorId = lessonMapper.selectCreatorIdById(lessonId);
        // 2.2 为空说明课程不存在
        if (creatorId == null) {
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "课程不存在");
        }
        // 2.3 相等则说明是创建者
        if (creatorId.longValue() == userId.longValue()) {
            return Result.ok(EduConstants.LESSON_IDENTITY_CREATOR);
        }

        // 3.查询是否为教学团队成员
        long count = lessonTchService.count(new LambdaUpdateWrapper<LessonTch>()
                .eq(LessonTch::getLessonId, lessonId)
                .eq(LessonTch::getUserId, userId));
        // 存在说明是教师
        if (count != 0) {
            return Result.ok(EduConstants.LESSON_IDENTITY_TCH);
        }

        // 4.查询是否为班级普通成员
        if (lessonClassStuService.existsByLessonIdAndUserId(lessonId, userId)) {
            return Result.ok(EduConstants.LESSON_IDENTITY_COMMON);
        }

        // 5.如果都不是则说明对该课程而言是游客
        return Result.ok(EduConstants.LESSON_IDENTITY_VISITOR);
    }

    @Override
    public Result getChapterAndSectionInfo(Long lessonId) {
        // 1.校验lessonId
        if (RegexUtils.isSnowIdInvalid(lessonId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "课程id格式错误");
        }
        // 2.判断课程是否存在
        boolean exists = lessonMapper.exists(new LambdaQueryWrapper<Lesson>().eq(Lesson::getId, lessonId));
        if (!exists) {
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "课程不存在");
        }
        // 3.查询章节信息
        List<ChapterNameInfoWithSectionListQuery> infoList = lessonMapper.selectChapterAndSectionInfo(lessonId);
        // 4.返回
        return Result.ok(infoList);
    }

    @Override
    public Result getTchLessonSimpleInfoList() {
        List<CreateOrTchLessonSimpleInfoQuery> infoList = lessonMapper.selectTchLessonSimpleInfoList(UserHolderUtils.getUserId());
        return Result.ok(infoList);
    }
}
