package com.zhulang.waveedu.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author 阿东
 * @date 2023/3/4 [14:47]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Boolean system;
    private String sendUser;
    private String toUser;

    private String text;

    private Timestamp time;
}
