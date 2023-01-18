package com.zhulang.waveedu.basic.service.impl;

import com.zhulang.waveedu.basic.service.UserService;
import com.zhulang.waveedu.basic.vo.PhoneCodeVO;
import com.zhulang.waveedu.common.entity.Result;
import org.springframework.stereotype.Service;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 23:31
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public Result loginByCode(PhoneCodeVO phoneCodeVO) {
        return Result.ok();
    }
}
