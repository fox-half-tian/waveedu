package com.zhulang.waveedu.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.basic.dao.UserInfoMapper;
import com.zhulang.waveedu.basic.po.UserInfo;
import com.zhulang.waveedu.basic.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * @author 狐狸半面添
 * @create 2023-01-19 0:11
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
}
