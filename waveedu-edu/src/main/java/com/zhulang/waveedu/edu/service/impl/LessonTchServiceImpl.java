package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import com.zhulang.waveedu.edu.po.Lesson;
import com.zhulang.waveedu.edu.po.LessonTch;
import com.zhulang.waveedu.edu.dao.LessonTchMapper;
import com.zhulang.waveedu.edu.query.LessonCacheQuery;
import com.zhulang.waveedu.edu.query.TchInviteCodeQuery;
import com.zhulang.waveedu.edu.service.LessonService;
import com.zhulang.waveedu.edu.service.LessonTchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 课程与教学团队的对应表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-03
 */
@Service
public class LessonTchServiceImpl extends ServiceImpl<LessonTchMapper, LessonTch> implements LessonTchService {
    @Resource
    private LessonTchMapper lessonTchMapper;
    @Resource
    private LessonService lessonService;
    @Resource
    private RedisCacheUtils redisCacheUtils;

    @Override
    public boolean isExistByLessonAndUser(Long lessonId, Long userId) {
        return lessonTchMapper.isExistByLessonAndUser(lessonId, userId) != null;
    }

    @Override
    public Result joinTchTeam(String encryptCode) {
        // 1.判断邀请码是否为空
        if (!StringUtils.hasText(encryptCode)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效邀请码");
        }
        // 3.解密邀请码
        String codeInfo = CipherUtils.decrypt(encryptCode);
        String[] decrypt = WaveStrUtils.strSplitToArr(codeInfo, "-");
        // 4.为空说明无效
        if (decrypt == null) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效邀请码");
        }
        // 5.获取 课程id 和 教学邀请码
        Long lessonId = null;
        String tchInviteCode = null;
        try {
            lessonId = Long.parseLong(decrypt[0]);
            tchInviteCode = decrypt[1];
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效邀请码");
        }
        // 6.获取课程的教学邀请码信息
        TchInviteCodeQuery info = lessonService.getTchInviteCodeById(lessonId);
        // 7.获取为空 或 禁用 或 邀请码不对-> 返回前端
        if (info == null || info.getCodeIsForbidden() == 1 || !info.getTchInviteCode().equals(tchInviteCode)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效邀请码");
        }
        // 8.判断是否已经存在教师团队中，已在 -> 返回前端
        if (isExistByLessonAndUser(lessonId, UserHolderUtils.getUserId())) {
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "已加入该课程教学团队，勿重复操作");
        }
        // 9.加入团队
        LessonTch lessonTch = new LessonTch();
        lessonTch.setLessonId(lessonId);
        lessonTch.setUserId(UserHolderUtils.getUserId());
        this.save(lessonTch);
        // 10.成功返回，将课程id返回
        return Result.ok(lessonId);
    }

    @Override
    public Result isLessonTch(Long lessonId, Long userId) {
        // 1.课程在redis是否存在
        boolean lessonExist = redisCacheUtils.existKey(RedisConstants.LESSON_INFO_KEY + lessonId);
        // 2.缓存中不存在，就去数据库中找
        if (!lessonExist) {
            LessonCacheQuery cacheInfo = lessonService.getCacheInfo(lessonId);
            if (cacheInfo == null) {
                // 2.1 数据库中没找到，说明不存在
                return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "课程不存在");
            } else {
                // 2.2 数据库中找到，就缓存到redis中
                redisCacheUtils.setCacheMap(RedisConstants.LESSON_INFO_KEY + lessonId, BeanUtil.beanToMap(cacheInfo, false, true));
            }
        }
        // 3.判断当前用户是否在教学团队中
        boolean exist = this.isExistByLessonAndUser(lessonId, userId);
        if (!exist) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        return null;
    }
}
