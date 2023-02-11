package com.zhulang.waveedu.edu.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import com.zhulang.waveedu.edu.constant.EduConstants;
import com.zhulang.waveedu.edu.dao.LessonMapper;
import com.zhulang.waveedu.edu.po.Lesson;
import com.zhulang.waveedu.edu.po.LessonTch;
import com.zhulang.waveedu.edu.query.LessonBasicInfoQuery;
import com.zhulang.waveedu.edu.query.TchInviteCodeQuery;
import com.zhulang.waveedu.edu.service.LessonService;
import com.zhulang.waveedu.edu.service.LessonTchService;
import com.zhulang.waveedu.edu.vo.SaveLessonVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;

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
        LessonBasicInfoQuery info = lessonMapper.selectBasicInfo(lessonId);
        if (info == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
        }
        return Result.ok(info);
    }

    @Override
    public Result getTchInviteCode(Long lessonId) {
        // 1.lessonId是否合理
        if (RegexUtils.isSnowIdInvalid(lessonId)){
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
        }

        // 2.当前用户是否为教师团队一员，规定只有该课程的教师才可以获取邀请码
        if (!lessonTchService.isExistByLessonAndUser(lessonId,UserHolderUtils.getUserId())){
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(),HttpStatus.HTTP_FORBIDDEN.getValue());
        }

        // 3.获取邀请码
        TchInviteCodeQuery tchInviteCodeQuery = lessonMapper.selectTchInviteCodeById(lessonId);

        // 4.获取不到说明不存在 -> 返回前端
        if (tchInviteCodeQuery==null){
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(),"找不到课程信息");
        }

        // 5.邀请码被禁用 -> 返回前端
        if (tchInviteCodeQuery.getCodeIsForbidden()==1){
            return Result.error(HttpStatus.HTTP_INFO_REFUSE.getCode(),"邀请码已被禁用");
        }

        // 6.对邀请码进行处理
        String encryptCode = CipherUtils.encrypt(lessonId + "-" + tchInviteCodeQuery.getTchInviteCode());

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
        if (RegexUtils.isSnowIdInvalid(lessonId)){
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
        }

        // 2.当前用户是否为教师团队一员，规定只有该课程的教师才可以修改邀请码
        if (!lessonTchService.isExistByLessonAndUser(lessonId,UserHolderUtils.getUserId())){
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(),HttpStatus.HTTP_FORBIDDEN.getValue());
        }

        // 4.查询原邀请码信息
        TchInviteCodeQuery codeInfo = lessonMapper.selectTchInviteCodeById(lessonId);
        // 5.为空 -> 说明课程已被删除
        if (codeInfo==null){
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
        }
        // 6.查询是否被禁用 -> 被禁用，返回前端
        if (codeInfo.getCodeIsForbidden()==1){
            return Result.error(HttpStatus.HTTP_INFO_REFUSE.getCode(),"邀请码已被禁用");
        }
        // 7.生成新的真实邀请码
        String code = RandomUtil.randomString(6);
        while (code.equals(codeInfo.getTchInviteCode())){
            code = RandomUtil.randomString(6);
        }
        // 8.修改数据库信息
        LambdaUpdateWrapper<Lesson> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Lesson::getTchInviteCode,code)
                        .eq(Lesson::getId,lessonId);
        this.update(wrapper);
        // 9.返回给前端加密后的邀请码
        return Result.ok(CipherUtils.encrypt(lessonId+"-"+code));
    }

    @Override
    public Result switchTchInviteCode(Long lessonId) {
        // 1.lessonId是否合理
        if (RegexUtils.isSnowIdInvalid(lessonId)){
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
        }
        // 2.当前用户是否为教师团队一员，规定只有该课程的教师才可以启用/禁用邀请码
        if (!lessonTchService.isExistByLessonAndUser(lessonId,UserHolderUtils.getUserId())){
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(),HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 4.查询邀请码信息
        TchInviteCodeQuery codeInfo = lessonMapper.selectTchInviteCodeById(lessonId);
        // 5.为空 -> 说明课程已被删除
        if (codeInfo==null){
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "找不到课程信息");
        }
        // 6.当前情况
        LambdaUpdateWrapper<Lesson> wrapper = new LambdaUpdateWrapper<>();
        if (codeInfo.getCodeIsForbidden()==0){
            // 6.1 说明已启用 -> 修改为禁用
            wrapper.set(Lesson::getCodeIsForbidden,1)
                    .eq(Lesson::getId,lessonId);
            this.update(wrapper);
            // 返回
            HashMap<String, String> map = new HashMap<>(1);
            map.put("status","off");
            return Result.ok(map);
        }else{
            // 6.2 说明已禁用 -> 修改为启用
            wrapper.set(Lesson::getCodeIsForbidden,0)
                    .eq(Lesson::getId,lessonId);
            this.update(wrapper);
            // 返回
            HashMap<String, String> map = new HashMap<>(2);
            map.put("status","on");
            map.put("tchInviteCode",CipherUtils.encrypt(lessonId+"-"+codeInfo.getTchInviteCode()));
            return Result.ok(map);
        }

    }

    @Override
    public Long getCreatorIdByLessonId(Long id) {
        return lessonMapper.selectCreatorIdById(id);
    }
}
