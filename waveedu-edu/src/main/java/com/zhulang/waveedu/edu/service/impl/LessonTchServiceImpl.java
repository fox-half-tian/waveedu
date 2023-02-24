package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.InviteCodeTypeConstants;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.*;
import com.zhulang.waveedu.edu.po.LessonTch;
import com.zhulang.waveedu.edu.dao.LessonTchMapper;
import com.zhulang.waveedu.edu.query.LessonCacheQuery;
import com.zhulang.waveedu.edu.query.LessonTchInfoQuery;
import com.zhulang.waveedu.edu.query.TchInviteCodeQuery;
import com.zhulang.waveedu.edu.service.LessonService;
import com.zhulang.waveedu.edu.service.LessonTchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
    public Result joinTchTeam(Long lessonId,String inviteCode) {
        // 1.获取课程的教学邀请码信息
        TchInviteCodeQuery info = lessonService.getTchInviteCodeById(lessonId);
        // 2.获取为空 或 禁用 或 邀请码不对-> 返回前端
        if (info == null || info.getCodeIsForbidden() == 1 || !info.getTchInviteCode().equals(inviteCode)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效邀请码");
        }
        // 3.判断是否已经存在教师团队中，已在 -> 返回前端
        if (isExistByLessonAndUser(lessonId, UserHolderUtils.getUserId())) {
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "已加入该课程教学团队，勿重复操作");
        }
        // 4.加入团队
        LessonTch lessonTch = new LessonTch();
        lessonTch.setLessonId(lessonId);
        lessonTch.setUserId(UserHolderUtils.getUserId());
        this.save(lessonTch);
        // 5.成功返回，将 邀请码类型 和 课程id 返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", InviteCodeTypeConstants.LESSON_TCH_TEAM_CODE_TYPE);
        map.put("id",lessonId);
        return Result.ok(map);
    }

    @Override
    public Result isLessonTch(Long lessonId, Long userId) {
        // 1.课程在redis是否存在
        boolean lessonExist = redisCacheUtils.existKey(RedisConstants.LESSON_INFO_KEY + lessonId);
        // 2.缓存中不存在，就去数据库中找
        if (!lessonExist) {
            LessonCacheQuery cacheInfo = lessonService.getNeedCacheInfo(lessonId);
            if (cacheInfo == null) {
                // 2.1 数据库中没找到，说明不存在
                return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "课程不存在");
            } else {
                // 2.2 数据库中找到，就缓存到redis中，ttl为10分钟
                redisCacheUtils.setCacheMap(RedisConstants.LESSON_INFO_KEY + lessonId, BeanUtil.beanToMap(cacheInfo, false, true));
                redisCacheUtils.expire(RedisConstants.LESSON_INFO_KEY + lessonId, RedisConstants.LESSON_INFO_TTL);
            }
        }
        // 3.判断当前用户是否在教学团队中
        boolean exist = this.isExistByLessonAndUser(lessonId, userId);
        if (!exist) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        return null;
    }

    @Override
    public Result getTchTeam(Long lessonId) {
        // 1.校验格式
        if (RegexUtils.isSnowIdInvalid(lessonId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "课程id格式错误");
        }
        // 2.查询创建者
        Long creatorId = lessonService.getCreatorIdByLessonId(lessonId);
        if (creatorId == null) {
            // 为空说明不存在或者课程已被删除
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "未找到课程信息");
        }
        // 2.查询教学团队 --> 由于上面代码的判断则 infoList 不可能为空
        List<LessonTchInfoQuery> infoList = lessonTchMapper.selectTchTeamInfo(lessonId);

        // 4.判断是否为创建者
        for (int i = 0; i < infoList.size(); i++) {
            if (infoList.get(i).getUserId().longValue() == creatorId.longValue()) {
                if (i != 0) {
                    Collections.swap(infoList, 0, i);
                }
                break;
            }
        }
        // 5.返回
        return Result.ok(infoList);


    }

}
