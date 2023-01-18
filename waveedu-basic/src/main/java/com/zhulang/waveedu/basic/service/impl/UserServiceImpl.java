package com.zhulang.waveedu.basic.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.basic.constant.BasicConstants;
import com.zhulang.waveedu.basic.dao.UserMapper;
import com.zhulang.waveedu.basic.po.User;
import com.zhulang.waveedu.basic.po.UserInfo;
import com.zhulang.waveedu.basic.service.UserInfoService;
import com.zhulang.waveedu.basic.service.UserService;
import com.zhulang.waveedu.basic.vo.PhoneCodeVO;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.BasicConvertUtils;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import com.zhulang.waveedu.common.util.RedisLockUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * ServiceImpl实现了IService，提供了IService中基础功能的实现
 * 若ServiceImpl无法满足业务需求，则可以使用自定的UserService定义方法，并在实现类中实现
 *
 * @author 狐狸半面添
 * @create 2023-01-17 23:31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private RedisCacheUtils redisCacheUtils;
    @Resource
    private RedisLockUtils redisLockUtils;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserInfoService userInfoService;

    @Override
    public Result loginByCode(PhoneCodeVO phoneCodeVO) {
        String phone = phoneCodeVO.getPhone();
        String code = phoneCodeVO.getCode();
        String lockKey = RedisConstants.LOCK_LOGIN_USER_CODE_KEY + phone;
        String codeKey = RedisConstants.LOGIN_USER_CODE_KEY + phone;
        boolean lock = false;
        try {
            // 1.拿到锁，设置TTL
            lock = redisLockUtils.tryLock(lockKey, RedisConstants.LOCK_LOGIN_USER_CODE_TTL);
            // 2.获取锁失败，直接退出
            if (!lock) {
                return Result.error(HttpStatus.HTTP_TRY_AGAIN_LATER.getCode(), HttpStatus.HTTP_TRY_AGAIN_LATER.getValue());
            }
            // 3.查询键值对信息：cacheInfo[0] 是 code ，cacheInfo[1] 是 count
            String value = redisCacheUtils.getCacheObject(codeKey);
            if (value == null) {
                return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "验证码失效，请重新发送");
            }
            String[] cacheInfo = BasicConvertUtils.strSplitToArr(value, ",");
            // 4.校验验证码
            if (!cacheInfo[0].equals(code)) {
                // 4.1 验证码不正确，count++
                int count = Integer.parseInt(cacheInfo[0]);
                count++;

                if (count >= RedisConstants.LOGIN_MAX_VERIFY_CODE_COUNT) {
                    // 次数大于等于设置的最大次数，验证码失效，移除 redis中 的验证码缓存
                    redisCacheUtils.deleteObject(codeKey);
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "多次校验失败，验证码已失效，请重新发送");
                } else {
                    // 如果不是，就修改缓存中的信息
                    Long expire = redisCacheUtils.getExpire(codeKey);
                    if (expire == null || expire <= 0) {
                        redisCacheUtils.setCacheObject(codeKey, cacheInfo[0] + "," + count, expire);
                    }
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "验证码错误");
                }
            }

            // 4.2 校验成功，继续往后走

            // 5.查看MySQL数据库是否有该用户信息
            Long id = userMapper.selectIdByPhone(phone);
            UserInfo userInfo = null;
            if (id == null) {
                // 不存在-->创建该用户
                // 获取代理对象(事务)-->事务生效
                UserService proxy = (UserService) AopContext.currentProxy();
                userInfo = proxy.register(phone);
            } else {
                // 存在 --> 直接查询该用户的信息

            }

            return Result.ok();
        } finally {
            // 最后释放锁
            if (lock) {
                redisLockUtils.unlock(lockKey);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo register(String phone) {
        User user = new User();
        user.setPhone(phone);
        // 在 basic_user 表中保存用户
        save(user);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setName("用户" + RandomUtil.randomString(8));
        userInfo.setIcon(BasicConstants.DEFAULT_USER_ICON);
        // 在 basic_user_info 表中保存用户
        userInfoService.save(userInfo);
        int a = 10 / 0;
        return userInfo;
    }


}
