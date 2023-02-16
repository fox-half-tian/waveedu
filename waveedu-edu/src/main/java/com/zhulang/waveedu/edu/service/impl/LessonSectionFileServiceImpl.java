package com.zhulang.waveedu.edu.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.constant.EduConstants;
import com.zhulang.waveedu.edu.po.LessonSectionFile;
import com.zhulang.waveedu.edu.dao.LessonSectionFileMapper;
import com.zhulang.waveedu.edu.query.SectionFileInfoQuery;
import com.zhulang.waveedu.edu.service.LessonSectionFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonSectionService;
import com.zhulang.waveedu.edu.vo.SaveSectionFileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 课程小节的文件表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-16
 */
@Service
public class LessonSectionFileServiceImpl extends ServiceImpl<LessonSectionFileMapper, LessonSectionFile> implements LessonSectionFileService {
    @Resource
    private LessonSectionFileMapper lessonSectionFileMapper;
    @Resource
    private LessonSectionService lessonSectionService;


    @Override
    public Result saveFile(SaveSectionFileVO saveSectionFileVO) {
        // 1.判断是否是教师团队成员，并一带判断小节、章节、课程是否存在
        Result result = lessonSectionService.isLessonTch(saveSectionFileVO.getSectionId(), saveSectionFileVO.getUserId());
        if (result != null) {
            return result;
        }
        // 2.解密文件信息
        LessonSectionFile lessonSectionFile;
        try {
            lessonSectionFile = JSON.parseObject(CipherUtils.decrypt(saveSectionFileVO.getFileInfo()), LessonSectionFile.class);
            if (lessonSectionFile == null) {
                return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "错误的文件信息");
            }
            // 判断类型
            if ("video".equals(lessonSectionFile.getFileFormat().split("/")[0])) {
                // 将类型设置为0-视频
                lessonSectionFile.setType(EduConstants.LESSON_SECTION_FILE_TYPE_VIDEO);
            } else {
                lessonSectionFile.setType(EduConstants.LESSON_SECTION_FILE_TYPE_OTHER);
            }

        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "错误的文件信息");
        }
        // 3.封装信息
        BeanUtils.copyProperties(saveSectionFileVO, lessonSectionFile);
        // 4.简单处理，前后无空格
        lessonSectionFile.setFileName(lessonSectionFile.getFileName().trim());
        // 5.保存到数据库
        lessonSectionFileMapper.insert(lessonSectionFile);
        // 6.返回
        return Result.ok(lessonSectionFile.getId());
    }

    @Override
    public Result removeFile(Integer fileId) {
        // 1.判断文件是否存在，并获取到 sectionId
        Integer sectionId = lessonSectionFileMapper.selectSectionIdById(fileId);
        if (sectionId == null) {
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "找不到该文件");
        }
        // 2.判断是否是教师团队成员，并一带判断小节、章节、课程是否存在
        Result result = lessonSectionService.isLessonTch(sectionId, UserHolderUtils.getUserId());
        if (result != null) {
            return result;
        }
        // 3.删除文件
        int recordCount = lessonSectionFileMapper.deleteById(fileId);
        return recordCount != 0 ? Result.ok() : Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(), "找不到该文件");
    }

    @Override
    public Result getSectionFileList(Integer sectionId) {
        // 0.校验Id
        if (sectionId < 1) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "小节id校验错误");
        }
        // 1.获取视频信息列表
        List<SectionFileInfoQuery> videoList = lessonSectionFileMapper.selectFileInfoBySectionIdAndType(sectionId, EduConstants.LESSON_SECTION_FILE_TYPE_VIDEO);
        // 2.获取其它信息列表
        List<SectionFileInfoQuery> otherList = lessonSectionFileMapper.selectFileInfoBySectionIdAndType(sectionId, EduConstants.LESSON_SECTION_FILE_TYPE_OTHER);
        // 3.返回
        HashMap<String, List<SectionFileInfoQuery>> map = new HashMap<>(2);
        map.put("video", videoList);
        map.put("other", otherList);
        return Result.ok(map);
    }
}
