package com.zhulang.waveedu.basic.service;

import com.zhulang.waveedu.basic.vo.PhoneCodeVO;
import com.zhulang.waveedu.common.entity.Result;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 23:30
 */
public interface UserService {

    /**
     * 通过手机验证码方式进行登录&注册
     *
     * @param phoneCodeVO 手机号+验证码
     * @return 验证结果
     */
    Result loginByCode(PhoneCodeVO phoneCodeVO);
}
