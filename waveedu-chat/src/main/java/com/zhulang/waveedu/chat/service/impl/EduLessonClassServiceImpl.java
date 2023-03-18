package com.zhulang.waveedu.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zhulang.waveedu.chat.dao.EduLessonClassMapper;
import com.zhulang.waveedu.chat.po.EduLessonClass;
import com.zhulang.waveedu.chat.query.CLassLastRecordInfoQuery;
import com.zhulang.waveedu.chat.query.ClassSimpleInfoQuery;
import com.zhulang.waveedu.chat.service.ChatClassRecordService;
import com.zhulang.waveedu.chat.service.EduLessonClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.chat.utils.UserHolder;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程班级表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-18
 */
@Service
public class EduLessonClassServiceImpl extends ServiceImpl<EduLessonClassMapper, EduLessonClass> implements EduLessonClassService {
    @Resource
    private EduLessonClassMapper eduLessonClassMapper;
    @Resource
    private ChatClassRecordService chatClassRecordService;

    @Override
    public Result getClassInfoList() {
        // 1.获取加入的班级信息
        List<ClassSimpleInfoQuery> classInfoList = eduLessonClassMapper.selectJoinClassInfoList(UserHolder.getUserId());
        if (classInfoList==null||classInfoList.isEmpty()){
            return Result.ok();
        }
        // 2.获取所有加入的班级的id
        List<Long> classIds = new ArrayList<>(classInfoList.size());
        for (ClassSimpleInfoQuery infoQuery : classInfoList) {
            classIds.add(infoQuery.getClassId());
        }
        // 3.获取每个班级的最后一条信息
        List<CLassLastRecordInfoQuery> classLastRecordInfoList = chatClassRecordService.getClassLastRecordInfoList(classIds);
        // 4.进行班级与消息的匹配
        ArrayList<ClassSimpleInfoQuery> hasMsgClassInfo = new ArrayList<>();
        ArrayList<ClassSimpleInfoQuery> noMsgClassInfo = new ArrayList<>();
        for (ClassSimpleInfoQuery classInfo : classInfoList) {
            for (CLassLastRecordInfoQuery lastRecordInfoQuery : classLastRecordInfoList) {
                if (classInfo.getClassId().equals(lastRecordInfoQuery.getClassId())){
                    BeanUtils.copyProperties(lastRecordInfoQuery,classInfo);
                    hasMsgClassInfo.add(classInfo);
                    break;
                }
            }
            if (classInfo.getMsgType()==null){
                classInfo.setMsgType(1);
                classInfo.setMsgContent("暂无消息");
                noMsgClassInfo.add(classInfo);
            }
        }
        // 5.按照时间进行排序
        List<ClassSimpleInfoQuery> sortedClassInfo = hasMsgClassInfo.stream().sorted((o1, o2) -> o1.getMsgSendTime().isBefore(o2.getMsgSendTime()) ? 1 : -1).collect(Collectors.toList());
        // 6.整合
        sortedClassInfo.addAll(noMsgClassInfo);
        // 7.返回
        return Result.ok(sortedClassInfo);
    }
}
