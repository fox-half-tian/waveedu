package com.zhulang.waveedu.chat;

import com.zhulang.waveedu.chat.dao.BasicUserMapper;
import com.zhulang.waveedu.chat.dao.ChatClassRecordMapper;
import com.zhulang.waveedu.chat.pojo.BasicUserInfo;
import com.zhulang.waveedu.chat.pojo.ChatClassRecord;
import com.zhulang.waveedu.chat.utils.SnowflakeIdWorker;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author 阿东
 * @date 2023/3/9 [23:10]
 */
@SpringBootTest
public class ChatTest {
    @Autowired
    private ChatClassRecordMapper chatClassRecordMapper;

    @Autowired
    private BasicUserMapper basicUserMapper;

    @Test
    public void test1() {
        val snowId = new SnowflakeIdWorker(1, 1).nextId();
        ChatClassRecord chatClassRecord = new ChatClassRecord();
        chatClassRecord.setId(snowId);
        chatClassRecord.setClassId(1125L);
        chatClassRecord.setCreateTime(new Timestamp(System.currentTimeMillis()));
        chatClassRecord.setContent("this is a message");
        chatClassRecord.setType(1);
        chatClassRecord.setTypeName("abc");
        chatClassRecord.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        chatClassRecord.setUserId(1125588700L);
        chatClassRecordMapper.insertChatMessage(chatClassRecord);
    }

    @Test
    public void test2() {
        BasicUserInfo basicUserInfo = basicUserMapper.getBasicUserInfo(1622520025167032321L).get(0);
        System.out.println(basicUserInfo);
    }

    @Test
    public void test3() {
        List<ChatClassRecord> allByClassId = chatClassRecordMapper.getAllByClassId(1125L, 4);
        System.out.println(allByClassId);
    }
}
