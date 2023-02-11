package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.RegexUtils;
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
        Result result = lessonTchService.isLessonTch(saveLessonFileVO.getLessonId(), saveLessonFileVO.getUserId());
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
    public Result removeFile(Long lessonFileId) {
        // 1.校验lessonFileId 是否合理
        if (RegexUtils.isSnowIdInvalid(lessonFileId)){
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(),"无效课程资料id");
        }
        // 2.获取该课程资料对应的课程id
        Long lessonId = lessonFileMapper.selectLessonIdById(lessonFileId);
        // 3.课程id为空说明不存在该课程资料id
        if (lessonId==null){
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(),"无效课程资料id");
        }
        // 4.判断是否为该课程的教师成员
        Result result = lessonTchService.isLessonTch(lessonId, UserHolderUtils.getUserId());
        if (result != null) {
            return result;
        }
        // 5.删除资料
        lessonFileMapper.deleteById(lessonFileId);

        // 6.返回成功
        return Result.ok();
    }


}
