package com.zhulang.waveedu.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.chat.po.EduLessonClass;
import com.zhulang.waveedu.chat.pojo.ChatClassRecord;
import com.zhulang.waveedu.chat.query.CLassLastRecordInfoQuery;
import com.zhulang.waveedu.chat.query.ClassSimpleInfoQuery;
import com.zhulang.waveedu.common.entity.Result;

import java.util.List;

/**
 * @author 狐狸半面添
 * @since 2023-03-18
 */
public interface ChatClassRecordService extends IService<ChatClassRecord> {
    /**
     * 获取所有班级的最后一条记录的信息
     *
     * @param classIds 班级id
     * @return 记录列表
     */
    List<CLassLastRecordInfoQuery> getClassLastRecordInfoList(List<Long> classIds);
}
