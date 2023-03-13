package com.zhulang.waveedu.chat.Message;

import com.zhulang.waveedu.chat.pojo.BasicUserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 阿东
 * @date 2023/3/13 [4:19]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineMessage {
    /**
     * 消息类型(0-系统消息，1-普通消息，2-历史消息，3-错误消息，4-在线信息)
     */
    private Integer messageType;
    /**
     * 在线人员信息
     */
    private List<BasicUserInfo> onlinePeople;
    /**
     * 不在线人员信息
     */
    private List<BasicUserInfo> notOnlinePeople;
}
