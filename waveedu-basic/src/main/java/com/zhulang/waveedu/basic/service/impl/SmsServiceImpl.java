package com.zhulang.waveedu.basic.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.zhulang.waveedu.basic.service.SmsService;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.RedisUser;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.SmsTemplateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 23:40
 */
@Service
public class SmsServiceImpl implements SmsService {
    @Resource
    private RedisCacheUtils redisCacheUtils;
    @Resource
    private SmsTemplateUtils smsTemplateUtils;

    @Override
    public Result sendLoginCode(String phone) {
        // 1.校验手机号格式
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "手机号格式错误");
        }

        String key = RedisConstants.LOGIN_USER_CODE_KEY + phone;

        // 2.查看缓存中是否已经存在，得到剩余TTL
        Long expire = redisCacheUtils.getExpire(key);

        // 3.存在并且剩余时长大于4分钟则不可再次发送验证码
        if(expire > RedisConstants.LOGIN_USER_CODE_AGAIN_TTL){
            return Result.error(HttpStatus.HTTP_TRY_AGAIN_LATER.getCode(),"验证码在有效期内");
        }

        // 4.验证码不存在或者剩余时长小于四分钟，则可以继续发送验证码 --> 先生成六位随机数
        String code = RandomUtil.randomNumbers(6);

        // 关于恶意并发的问题，在短信云平台已经自动做了处理，这里就无需处理

        // 5.先存储到 redis，附带验证次数，初始化为0
        redisCacheUtils.setCacheObject(key,code+",0",RedisConstants.LOGIN_USER_CODE_TTL);

        // 6.发送短信到手机
        boolean result = smsTemplateUtils.sendLoginCode(phone, code);
        if(!result){
            // 6.1 发送失败，则移除 redis 中的验证码缓存信息，并返回
            redisCacheUtils.deleteObject(key + phone);
            return Result.error(HttpStatus.HTTP_INTERNAL_ERROR.getCode(),HttpStatus.HTTP_INTERNAL_ERROR.getValue());
        }

        // 6.2 发送成功
        return Result.ok();
    }
}
