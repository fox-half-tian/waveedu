package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import com.zhulang.waveedu.edu.constant.EduConstants;
import com.zhulang.waveedu.edu.dto.LessonFileDTO;
import com.zhulang.waveedu.edu.po.LessonFile;
import com.zhulang.waveedu.edu.dao.LessonFileMapper;
import com.zhulang.waveedu.edu.service.LessonClassStuService;
import com.zhulang.waveedu.edu.service.LessonFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonService;
import com.zhulang.waveedu.edu.service.LessonTchService;
import com.zhulang.waveedu.edu.query.LessonFileDetailInfoQuery;
import com.zhulang.waveedu.edu.query.LessonFileSimpleInfoQuery;
import com.zhulang.waveedu.edu.vo.lessonfilevo.SaveLessonFileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @Resource
    private LessonClassStuService lessonClassStuService;

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
        if (RegexUtils.isSnowIdInvalid(lessonFileId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效课程资料id");
        }
        // 2.校验只有老师才能操作
        Result result = verifyOfTch(lessonFileId);
        if (result != null) {
            return result;
        }
        // 3.删除资料
        lessonFileMapper.deleteById(lessonFileId);

        // 4.返回成功
        return Result.ok();
    }

    @Override
    public Result modifyFileName(Long fileId, String fileName) {
        // 1.校验只有老师才能操作
        Result result = verifyOfTch(fileId);
        if (result != null) {
            return result;
        }
        // 2.修改文件名
        LambdaUpdateWrapper<LessonFile> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LessonFile::getId, fileId)
                .set(LessonFile::getFileName, WaveStrUtils.removeBlank(fileName));
        return this.update(wrapper) ? Result.ok() : Result.error();
    }

    @Override
    public Result getSimpleInfoList(Long lessonId, Long fileId) {
        // 0.校验课程格式
        if (RegexUtils.isSnowIdInvalid(lessonId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "课程id格式错误");
        }

        // 1.判断 非空的fileId 是否格式正确
        if (fileId != null && RegexUtils.isSnowIdInvalid(fileId)) {
            fileId = null;
        }
        // 2.查询信息，按照时间由近及远
        List<LessonFileSimpleInfoQuery> simpleInfoList = lessonFileMapper.selectSimpleInfoList(lessonId, fileId, EduConstants.DEFAULT_LESSON_SIMPLE_FILE_LIST_QUERY_LIMIT);
        // 3.返回
        return Result.ok(simpleInfoList);
    }

    @Override
    public Result getDetailInfoList(Long lessonId, Long fileId) {
        // 0.校验课程格式
        if (RegexUtils.isSnowIdInvalid(lessonId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "课程id格式错误");
        }

        // 1.判断 非空的fileId 是否格式正确
        if (fileId != null && RegexUtils.isSnowIdInvalid(fileId)) {
            fileId = null;
        }
        // 2.查询信息，按照时间由近及远
        List<LessonFileDetailInfoQuery> detailInfoList = lessonFileMapper.selectDetailInfoList(lessonId, fileId, EduConstants.DEFAULT_LESSON_DETAIL_FILE_LIST_QUERY_LIMIT);
        // 3.返回
        return Result.ok(detailInfoList);
    }

    @Override
    public Result downloadLessonFile(Long lessonFileId) {
        // 1.校验格式
        if (RegexUtils.isSnowIdInvalid(lessonFileId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "课程文件id格式错误");
        }
        // 2.查询对应的课程
        Long lessonId = lessonFileMapper.selectLessonIdById(lessonFileId);
        if (lessonId==null){
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "文件不存在");
        }
        // 3.校验是否为教师团队成员或班级成员
        Long userId = UserHolderUtils.getUserId();
        if (!lessonTchService.isExistByLessonAndUser(lessonId, userId) && !lessonClassStuService.existsByLessonIdAndUserId(lessonId, userId)) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 4.增加次数
        lessonFileMapper.updateDownloadCountOfInsertOne(lessonFileId);
        // 5.获取文件路径与最新下载次数
        Map<String, Object> result = lessonFileMapper.selectFilePathAndDownLoadCount(lessonFileId);
        // 6.返回
        return Result.ok(result);
    }

    /**
     * 校验只有老师才能操作
     *
     * @param lessonFileId 文件id
     * @return 结果
     */
    private Result verifyOfTch(Long lessonFileId) {
        // 1.获取该课程资料对应的课程id
        Long lessonId = lessonFileMapper.selectLessonIdById(lessonFileId);
        // 2.课程id为空说明不存在该课程资料id
        if (lessonId == null) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效课程资料id");
        }
        // 3.判断是否为该课程的教师成员
        return lessonTchService.isLessonTch(lessonId, UserHolderUtils.getUserId());
    }
}
