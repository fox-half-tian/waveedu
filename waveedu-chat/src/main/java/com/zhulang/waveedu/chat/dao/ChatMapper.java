package com.zhulang.waveedu.chat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.chat.pojo.ChatClassRecord;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @author 阿东
 * @date 2023/3/8 [20:48]
 */
@Mapper
public interface ChatMapper extends BaseMapper<ChatClassRecord> {
    /**
     * 插入聊天信息
     * @param chat 信息
     */
    void insertChatMessage(@Param("chat")ChatClassRecord chat);

    /**
     * 通过班级ID获取班级群聊信息
     * @param classId 班级ID
     * @param nums 群聊信息数量
     * @return 班级信息
     */
    List<ChatClassRecord> getAllByClassId(@Param("classId")Long classId,@Param("nums")Integer nums);
}
