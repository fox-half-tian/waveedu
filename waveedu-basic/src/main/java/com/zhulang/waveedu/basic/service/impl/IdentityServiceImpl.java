package com.zhulang.waveedu.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.basic.dao.IdentityMapper;
import com.zhulang.waveedu.basic.po.Identity;
import com.zhulang.waveedu.basic.service.IdentityService;
import org.springframework.stereotype.Service;

/**
 * @author 狐狸半面添
 * @create 2023-02-03 0:35
 */
@Service
public class IdentityServiceImpl extends ServiceImpl<IdentityMapper, Identity> implements IdentityService {
}
