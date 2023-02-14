package com.zhulang.waveedu.edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.dao.UserInfoMapper;
import com.zhulang.waveedu.edu.po.UserInfo;
import com.zhulang.waveedu.edu.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 飒沓流星
 * @create 2023-01-28 17:52
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public String getIconByUserId(Long userId) {
        return userInfoMapper.selectIconByUserId(userId);
    }
}
