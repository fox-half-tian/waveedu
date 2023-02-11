package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.dto.LessonFileDTO;
import com.zhulang.waveedu.edu.po.Lesson;
import com.zhulang.waveedu.edu.po.LessonFile;
import com.zhulang.waveedu.edu.dao.LessonFileMapper;
import com.zhulang.waveedu.edu.service.LessonFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonService;
import com.zhulang.waveedu.edu.service.LessonTchService;
import com.zhulang.waveedu.edu.vo.SaveLessonFileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 课程学习资料表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-11
 */
@Service
public class LessonFileServiceImpl extends ServiceImpl<LessonFileMapper, LessonFile> implements LessonFileService {

    @Resource
    private LessonFileMapper lessonFileMapper;

    @Resource
    private LessonTchService lessonTchService;

    @Resource
    private LessonService lessonService;

    @Override
    public Result saveFile(SaveLessonFileVO saveLessonFileVO) {
        // 0.判断是否为该课程的教师成员
        Result result = isLessonTch(saveLessonFileVO.getLessonId(), saveLessonFileVO.getUserId());
        if (result != null) {
            return result;
        }

        // 1.解密文件信息
        LessonFile lessonFile;
        try {
            lessonFile = JSON.parseObject(CipherUtils.decrypt(saveLessonFileVO.getFileInfo()), LessonFile.class);
            if (lessonFile == null) {
                return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "错误的文件信息");
            }
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "错误的文件信息");
        }
        // 2.封装信息
        BeanUtils.copyProperties(saveLessonFileVO, lessonFile);
        // 3.简单处理，前后无空格
        lessonFile.setFileName(lessonFile.getFileName().trim());
        lessonFile.setDownloadCount(0);
        // 4.保存到数据库
        lessonFileMapper.insert(lessonFile);
        // 5.转换到dto中
        LessonFileDTO lessonFileDTO = BeanUtil.copyProperties(lessonFile, LessonFileDTO.class);
        lessonFileDTO.setUserName(UserHolderUtils.getUserName());
        // 6.返回信息
        return Result.ok(lessonFileDTO);
    }

    @Override
    public Result removeFile(Long lessonId, Long lessonFileId) {

        // 1.判断是否为该课程的教师成员
        Result result = isLessonTch(lessonId, UserHolderUtils.getUserId());
        if (result != null) {
            return result;
        }
        // 3.删除资料
        lessonFileMapper.deleteById(lessonFileId);

        // 4.返回成功
        return Result.ok();
    }

    /**
     * 判断是否为该课程的教师成员
     *
     * @param lessonId 课程id
     * @param userId   用户id
     * @return null-是的，如果not null，则不是
     */
    private Result isLessonTch(Long lessonId, Long userId) {
        // 0.1 课程是否存在
        long count = lessonService.count(new LambdaQueryWrapper<Lesson>().eq(Lesson::getId, lessonId));
        if (count == 0) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "课程不存在");
        }
        // 0.2 当前用户是否在教学团队中
        boolean exist = lessonTchService.isExistByLessonAndUser(lessonId, userId);
        if (!exist) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        return null;
    }

}
