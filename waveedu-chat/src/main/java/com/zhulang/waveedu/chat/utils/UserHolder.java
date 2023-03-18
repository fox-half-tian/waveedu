package com.zhulang.waveedu.chat.utils;

import com.zhulang.waveedu.common.entity.RedisUser;

/**
 * @author 狐狸半面添
 * @create 2023-03-18 17:31
 */
public class UserHolder {
    private static final ThreadLocal<RedisUser> tl = new ThreadLocal<>();

    public static void saveUser(RedisUser user){
        tl.set(user);
    }

    public static RedisUser getUser(){
        return tl.get();
    }

    public static Long getUserId(){
        return tl.get().getId();
    }

    public static void removeUser(){
        tl.remove();
    }
}
