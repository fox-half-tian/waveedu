package com.zhulang.waveedu.chat.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-18 16:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CLassLastRecordInfoQuery {
    /**
     * 班级id
     */
    private Long classId;
    /**
     * 消息类型
     */
    private Integer msgType;
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 消息发送时间
     */
    private LocalDateTime msgSendTime;
}
