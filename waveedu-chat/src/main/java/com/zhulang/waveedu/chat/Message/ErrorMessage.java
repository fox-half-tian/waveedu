package com.zhulang.waveedu.chat.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 阿东
 * @date 2023/3/12 [17:31]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    /**
     * 消息类型(0-系统消息，1-普通消息，2-历史消息，3-错误消息，4-在线信息)
     */
    private Integer messageType;
    /**
     * 错误信息
     */
    private String msg;
}
