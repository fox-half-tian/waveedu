package com.zhulang.waveedu.chat.Message;

import com.zhulang.waveedu.chat.pojo.ChatClassRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 阿东
 * @date 2023/3/4 [14:47]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    /**
     * 消息类型(0-系统消息，1-普通消息，2-历史消息，3-错误消息，4-在线信息)
     */
    private Integer messageType;
    private Integer page;
    /**
     * 消息内容
     */
    private ChatClassRecord chatMessage;

}
