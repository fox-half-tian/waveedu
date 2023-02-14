package com.zhulang.waveedu.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.po.UserInfo;
import org.springframework.stereotype.Service;


/**
 * @author 狐狸半面添
 * @create 2023-01-18 23:24
 */
@Service
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 根据用户id获取头像
     *
     * @param userId 用户id
     * @return 头像
     */
    String getIconByUserId(Long userId);
}