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
import com.zhulang.waveedu.edu.dto.LessonClassFileDTO;
import com.zhulang.waveedu.edu.dto.LessonFileDTO;
import com.zhulang.waveedu.edu.po.LessonClassFile;
import com.zhulang.waveedu.edu.dao.LessonClassFileMapper;
import com.zhulang.waveedu.edu.po.LessonFile;
import com.zhulang.waveedu.edu.service.LessonClassFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonClassService;
import com.zhulang.waveedu.edu.vo.classvo.SaveClassFileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 课程班级资料表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-25
 */
@Service
public class LessonClassFileServiceImpl extends ServiceImpl<LessonClassFileMapper, LessonClassFile> implements LessonClassFileService {
    @Resource
    private LessonClassFileMapper lessonClassFileMapper;

    @Resource
    private LessonClassService lessonClassService;

    @Override
    public Result saveFile(SaveClassFileVO saveClassFileVO) {
        // 1.判断是否为该班级的创建者
        if (!lessonClassService.existsByUserIdAndClassId(saveClassFileVO.getUserId(), saveClassFileVO.getLessonClassId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.解密文件信息
        LessonClassFile lessonClassFile;
        try {
            lessonClassFile = JSON.parseObject(CipherUtils.decrypt(saveClassFileVO.getFileInfo()), LessonClassFile.class);
            if (lessonClassFile == null) {
                return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "错误的文件信息");
            }
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "错误的文件信息");
        }
        // 3.封装信息
        BeanUtils.copyProperties(saveClassFileVO, lessonClassFile);
        // 4.简单处理，前后无空格
        lessonClassFile.setFileName(lessonClassFile.getFileName().trim());
        lessonClassFile.setDownloadCount(0);
        // 5.保存到数据库
        lessonClassFileMapper.insert(lessonClassFile);
        // 5.转换到dto中
        LessonClassFileDTO lessonClassFileDTO = BeanUtil.copyProperties(lessonClassFile, LessonClassFileDTO.class);
        lessonClassFileDTO.setUserName(UserHolderUtils.getUserName());
        // 6.返回信息
        return Result.ok(lessonClassFileDTO);
    }

    @Override
    public Result removeFile(Long lessonClassFileId) {
        // 1.校验lessonFileId 是否合理
        if (RegexUtils.isSnowIdInvalid(lessonClassFileId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级资料id格式错误");
        }
        // 2.判断是否为班级创建者，一并判断文件是否存在
        Result result = this.isClassCreator(lessonClassFileId);
        if (result != null) {
            return result;
        }
        // 3.删除资料
        lessonClassFileMapper.deleteById(lessonClassFileId);

        // 4.返回成功
        return Result.ok();
    }

    @Override
    public Result modifyFileName(Long lessonClassFileId, String fileName) {
        // 1.校验只班级创建者才能操作
        Result result = this.isClassCreator(lessonClassFileId);
        if (result != null) {
            return result;
        }
        // 2.修改文件名
        LambdaUpdateWrapper<LessonClassFile> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LessonClassFile::getId, lessonClassFileId)
                .set(LessonClassFile::getFileName, fileName.trim());
        return this.update(wrapper) ? Result.ok() : Result.error();
    }

    /**
     * 判断是否为班级创建者，一并判断文件是否存在
     *
     * @param lessonClassFileId 文件id
     * @return 判断信息
     */
    public Result isClassCreator(Long lessonClassFileId) {
        // 1.获取班级id
        Long lessonClassId = lessonClassFileMapper.selectLessonClassId(lessonClassFileId);
        // 2.判断是否存在 --> 不存在说明班级不存在
        if (lessonClassId == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "文件不存在");
        }
        // 3.判断是否为创建者
        if (!lessonClassService.existsByUserIdAndClassId(UserHolderUtils.getUserId(), lessonClassId)) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        return null;
    }
}
