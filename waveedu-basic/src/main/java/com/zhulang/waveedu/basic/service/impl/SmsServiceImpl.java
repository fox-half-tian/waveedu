package com.zhulang.waveedu.basic.service.impl;

import com.zhulang.waveedu.basic.service.SmsService;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.entity.Result;
import org.springframework.stereotype.Service;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 23:40
 */
@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public Result sendLoginCode(String phone) {
        // 校验手机号格式
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "手机号格式错误");
        }

        // 查看缓存中是否已经存在

        return Result.ok();
    }
}
