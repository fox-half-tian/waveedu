package com.zhulang.waveedu.edu.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import com.zhulang.waveedu.edu.dao.LessonSectionMapper;
import com.zhulang.waveedu.edu.po.LessonFile;
import com.zhulang.waveedu.edu.po.LessonSectionFile;
import com.zhulang.waveedu.edu.dao.LessonSectionFileMapper;
import com.zhulang.waveedu.edu.service.LessonSectionFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonSectionService;
import com.zhulang.waveedu.edu.vo.SaveSectionFileVO;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    public Result saveVideoFile(SaveSectionFileVO saveSectionFileVO) {
        // 1.判断是否是教师团队成员，并一带判断小节、章节、课程是否存在
        Result result = lessonSectionService.isLessonTch(saveSectionFileVO.getSectionId(), saveSectionFileVO.getUserId());
        if (result != null) {
            return result;
        }
        // 2.解密文件信息
        LessonSectionFile lessonSectionFile;
        try {
            lessonSectionFile = JSON.parseObject(CipherUtils.decrypt(saveSectionFileVO.getFileInfo()),LessonSectionFile.class);
            if (lessonSectionFile==null){
                return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(),"错误的文件信息");
            }
            // 如果不是video，则上传出错
            if (!"video".equals(lessonSectionFile.getFileFormat().split("/")[0])){
                return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(),"错误的文件信息");
            }
            // 将类型设置为0-视频
            lessonSectionFile.setType(0);
        }catch (Exception e){
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(),"错误的文件信息");
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
}
