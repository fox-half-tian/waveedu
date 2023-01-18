package com.zhulang.waveedu.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.basic.po.User;
import com.zhulang.waveedu.basic.po.UserInfo;
import com.zhulang.waveedu.basic.vo.PhoneCodeVO;
import com.zhulang.waveedu.common.entity.Result;

/**
 * UserService继承IService模板提供的基础功能
 *
 * @author 狐狸半面添
 * @create 2023-01-17 23:30
 */
public interface UserService extends IService<User> {

    /**
     * 通过手机验证码方式进行登录&注册
     *
     * @param phoneCodeVO 手机号+验证码
     * @return 验证结果
     */
    Result loginByCode(PhoneCodeVO phoneCodeVO);

    /**
     * 注册新用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    UserInfo register(String phone);
}
