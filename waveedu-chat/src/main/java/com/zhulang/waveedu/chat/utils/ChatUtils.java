package com.zhulang.waveedu.chat.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.chat.pojo.Message;

import java.sql.Timestamp;

/**
 * @author 阿东
 * @date 2023/3/4 [15:05]
 */
public  class ChatUtils {

    public static String encodingMessage(boolean system,String fromUser,String message,Timestamp timestamp){
        Message msg = new Message(system, fromUser, null, message, timestamp);
        return (String) JSONObject.toJSON(msg);
    }

}
