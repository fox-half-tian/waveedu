package com.zhulang.waveedu.basic.service.impl;

import com.zhulang.waveedu.basic.po.Admin;
import com.zhulang.waveedu.basic.dao.AdminMapper;
import com.zhulang.waveedu.basic.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-11
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
