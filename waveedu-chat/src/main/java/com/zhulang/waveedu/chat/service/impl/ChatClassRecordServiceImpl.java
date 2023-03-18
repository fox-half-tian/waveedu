package com.zhulang.waveedu.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.chat.dao.ChatClassRecordMapper;
import com.zhulang.waveedu.chat.pojo.ChatClassRecord;
import com.zhulang.waveedu.chat.query.CLassLastRecordInfoQuery;
import com.zhulang.waveedu.chat.query.ClassSimpleInfoQuery;
import com.zhulang.waveedu.chat.service.ChatClassRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程班级表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-18
 */
@Service
public class ChatClassRecordServiceImpl extends ServiceImpl<ChatClassRecordMapper, ChatClassRecord> implements ChatClassRecordService {
    @Resource
    private ChatClassRecordMapper chatClassRecordMapper;


    @Override
    public List<CLassLastRecordInfoQuery> getClassLastRecordInfoList(List<Long> classIds) {
        return chatClassRecordMapper.selectClassLastRecordInfoList(classIds);
    }
}
