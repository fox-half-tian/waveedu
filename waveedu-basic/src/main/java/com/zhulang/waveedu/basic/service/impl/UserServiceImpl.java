package com.zhulang.waveedu.basic.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.basic.constant.BasicConstants;
import com.zhulang.waveedu.basic.dao.UserMapper;
import com.zhulang.waveedu.basic.po.Logoff;
import com.zhulang.waveedu.basic.po.User;
import com.zhulang.waveedu.basic.po.UserInfo;
import com.zhulang.waveedu.basic.query.UserIdAndPasswordAndStatusQuery;
import com.zhulang.waveedu.basic.query.UserIdAndStatusQuery;
import com.zhulang.waveedu.basic.service.LogoffService;
import com.zhulang.waveedu.basic.service.UserInfoService;
import com.zhulang.waveedu.basic.service.UserService;
import com.zhulang.waveedu.basic.vo.LogoffVO;
import com.zhulang.waveedu.basic.vo.PhoneCodeVO;
import com.zhulang.waveedu.basic.vo.PhonePasswordVO;
import com.zhulang.waveedu.basic.vo.UpdatePwdVO;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.RedisUser;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.*;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
    @Resource
    private LogoffService logoffService;

    @Override
    public Result loginByCode(PhoneCodeVO phoneCodeVO) {
        String phone = phoneCodeVO.getPhone();
        String code = phoneCodeVO.getCode();
        String lockKey = RedisConstants.LOCK_LOGIN_USER_KEY + phone;
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
                return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "验证码已失效，请重新发送");
            }
            String[] cacheInfo = WaveStrUtils.strSplitToArr(value, ",");
            // 4.校验验证码
            if (!cacheInfo[0].equals(code)) {
                // 4.1 验证码不正确，count++
                int count = Integer.parseInt(cacheInfo[1]);
                count++;

                if (count >= BasicConstants.LOGIN_MAX_VERIFY_CODE_COUNT) {
                    // 次数大于等于设置的最大次数，验证码失效，移除 redis中 的验证码缓存
                    redisCacheUtils.deleteObject(codeKey);
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "多次校验失败，验证码已失效，请重新发送");
                } else {
                    // 如果不是，就修改缓存中的信息
                    Long expire = redisCacheUtils.getExpire(codeKey);
                    if (expire > 0) {
                        redisCacheUtils.setCacheObject(codeKey, cacheInfo[0] + "," + count, expire);
                    }
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "验证码错误");
                }
            }

            // 4.2 校验成功，继续往后走

            // 5.删除验证码缓存
            redisCacheUtils.deleteObject(codeKey);

            // 6.查看MySQL数据库是否有该用户信息
            UserIdAndStatusQuery userQuery = userMapper.selectIdAndStatusByPhone(phone);
            // 获取代理对象(事务)-->事务生效
            UserService proxy = (UserService) AopContext.currentProxy();
            UserInfo userInfo = null;
            if (userQuery == null) {
                // 7.不存在-->创建该用户
                userInfo = proxy.register(phone);
            } else {
                // 8.存在 --> 判断用户状态,获取用户信息
                if (userQuery.getStatus() == 1) {
                    // 说明处于注销冻结状态，则修改状态为正常状态
                    proxy.modifyStatusToNormal(userQuery.getId());
                }
                // 获取用户信息
                LambdaQueryWrapper<UserInfo> userInfoWrapper = new LambdaQueryWrapper<>();
                userInfoWrapper.select(UserInfo::getId, UserInfo::getName, UserInfo::getIcon)
                        .eq(UserInfo::getId, userQuery.getId());
                userInfo = userInfoService.getOne(userInfoWrapper);
            }

            // 9.缓存用户信息，携带 token 返回成功，以后请求时前端需要携带 token，放在请求头中
            return Result.ok(saveRedisInfo(userInfo));

        } finally {
            // 10.最后释放锁
            if (lock) {
                redisLockUtils.unlock(lockKey);
            }
        }
    }

    /**
     * 设置用户信息，缓存到 redis 中
     *
     * @param userInfo 用户基本信息
     * @return 生成的token
     */
    public String saveRedisInfo(UserInfo userInfo) {
        // todo 1.查询权限信息（后面再来补充）

        // 2.生成 token
        String uuid = UUID.randomUUID().toString(true);
        String token = CipherUtils.encrypt(userInfo.getId() + "-" + uuid);

        // 3.设置缓存的用户信息
        RedisUser redisUser = new RedisUser();
        redisUser.setUuid(uuid);
        redisUser.setTime(System.currentTimeMillis());
        redisUser.setIcon(userInfo.getIcon());
        redisUser.setName(userInfo.getName());
        redisUser.setId(userInfo.getId());
        // todo 设置权限

        // 4.信息存储到 redis
        redisCacheUtils.setCacheObject(RedisConstants.LOGIN_USER_INFO_KEY + redisUser.getId(), redisUser, RedisConstants.LOGIN_USER_INFO_TTL);

        // 5.返回token
        return token;
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
        userInfo.setName("逐浪者" + RandomUtil.randomString(8));
        userInfo.setIcon(BasicConstants.DEFAULT_USER_ICON);
        // 在 basic_user_info 表中保存用户
        userInfoService.save(userInfo);
        return userInfo;
    }

    /**
     * 修改用户状态，删除 basic_logoff 表中的对应数据
     *
     * @param id 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyStatusToNormal(Long id) {
        LambdaUpdateWrapper<User> userWrapper = new LambdaUpdateWrapper<>();
        userWrapper.eq(User::getId, id)
                .set(User::getStatus, 0);
        // 修改用户状态
        update(userWrapper);
        LambdaUpdateWrapper<Logoff> logoffWrapper = new LambdaUpdateWrapper<>();
        logoffWrapper.eq(Logoff::getUserId, id);
        // 删除 basic_logoff 表中的对应数据
        logoffService.remove(logoffWrapper);
    }

    @Override
    public Result loginByPassword(PhonePasswordVO phonePasswordVO) {
        String phone = phonePasswordVO.getPhone();
        String password = phonePasswordVO.getPassword();
        String lockKey = RedisConstants.LOCK_LOGIN_USER_KEY + phone;
        String phoneKey = RedisConstants.LOGIN_USER_PWD_KEY + phone;
        boolean lock = false;
        try {
            // 1.拿到锁，设置TTL
            lock = redisLockUtils.tryLock(lockKey, RedisConstants.LOCK_LOGIN_USER_CODE_TTL);
            // 2.获取锁失败，直接退出
            if (!lock) {
                return Result.error(HttpStatus.HTTP_TRY_AGAIN_LATER.getCode(), HttpStatus.HTTP_TRY_AGAIN_LATER.getValue());
            }
            // 3.从 redis 中获取当前手机号的登录次数
            Integer count = redisCacheUtils.getCacheObject(phoneKey);
            if (count == null) {
                count = 0;
            }
            // 4.次数超过8次，则返回稍后再试
            if (count >= BasicConstants.LOGIN_MAX_VERIFY_PWD_COUNT) {
                return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "账号因多次输入密码错误，已冻结" + RedisConstants.LOGIN_USER_PWD_LOCK_TTL / 60 + "分钟");
            }
            count++;
            // 5.从数据库中查询该手机号的用户信息
            UserIdAndPasswordAndStatusQuery userQuery = userMapper.selectIdAndPasswordAndStatusByPhone(phone);
            // 6.用户信息不存在则返回错误
            if (userQuery == null) {
                return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "手机号或密码错误");
            }

            // 7.校验密码
            Boolean matches = PasswordEncoderUtils.matches(userQuery.getPassword(), password);
            if (!matches) {
                // 8.校验失败的处理
                // 8.1 是否冻结手机号
                if (count >= BasicConstants.LOGIN_MAX_VERIFY_PWD_COUNT) {
                    // 冻结手机+密码方式登录15分钟
                    redisCacheUtils.setCacheObject(phoneKey, count, RedisConstants.LOGIN_USER_PWD_LOCK_TTL);
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "账号因多次输入密码错误，已冻结" + RedisConstants.LOGIN_USER_PWD_LOCK_TTL / 60 + "分钟");
                }
                // 8.2 未冻结，则修改将手机号验证次数
                if (count == 1) {
                    redisCacheUtils.setCacheObject(phoneKey, count, RedisConstants.LOGIN_USER_PWD_TTL);
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "手机号或密码错误");
                }
                Long expire = redisCacheUtils.getExpire(phoneKey);
                if (expire > 0) {
                    redisCacheUtils.setCacheObject(phoneKey, count, expire);
                }
                // 8.3 返回结果，当剩1-3次机会时返回剩余次数
                int surplusCount = BasicConstants.LOGIN_MAX_VERIFY_PWD_COUNT - count;
                if (surplusCount > 0 && surplusCount <= BasicConstants.LOGIN_PWD_MAX_SURPLUS_COUNT) {
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "手机号或密码错误，" + surplusCount + "次机会后冻结手机号15分钟");
                } else {
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "手机号或密码错误");
                }
            }
            // 校验成功则继续往后走

            // 9.判断是否在注销冻结期
            if (userQuery.getStatus() == 1) {
                // 只能通过手机号验证码方式登录来解除注销
                return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "当前用户处于注销冻结期，请使用验证码方式进行解冻登录");
            }
            // 10.查询用户信息
            LambdaQueryWrapper<UserInfo> userInfoWrapper = new LambdaQueryWrapper<>();
            userInfoWrapper.select(UserInfo::getId, UserInfo::getName, UserInfo::getIcon)
                    .eq(UserInfo::getId, userQuery.getId());
            UserInfo userInfo = userInfoService.getOne(userInfoWrapper);
            // 11.移除缓存中的次数统计
            redisCacheUtils.deleteObject(phoneKey);

            // 12.缓存用户信息，携带 token 返回成功，以后请求时前端需要携带 token，放在请求头中
            return Result.ok(saveRedisInfo(userInfo));
        } finally {
            // 13.最后释放锁
            if (lock) {
                redisLockUtils.unlock(lockKey);
            }
        }
    }

    @Override
    public Result logout(Long id) {
        redisCacheUtils.deleteObject(RedisConstants.LOGIN_USER_INFO_KEY + id);
        return Result.ok();
    }

    @Override
    public Result logoff(LogoffVO logoffVO) {
        // 拿到验证码
        String code = logoffVO.getCode();
        // 拿到注销原因
        String reason = "";
        if (StringUtils.hasText(logoffVO.getReason())) {
            // 如果是合法字符串，就去除前后空白符
            reason = logoffVO.getReason().trim();
        }
        Long userId = UserHolderUtils.getUserId();
        String lockKey = RedisConstants.LOCK_LOGOFF_USER_KEY + userId;
        String codeKey = RedisConstants.LOGOFF_USER_CODE_KEY + userId;
        boolean lock = false;
        try {
            // 1.拿到锁，设置TTL
            lock = redisLockUtils.tryLock(lockKey, RedisConstants.LOCK_LOGOFF_USER_CODE_TTL);
            // 2.获取锁失败，直接退出
            if (!lock) {
                return Result.error(HttpStatus.HTTP_TRY_AGAIN_LATER.getCode(), HttpStatus.HTTP_TRY_AGAIN_LATER.getValue());
            }

            // 3.查询键值对信息：cacheInfo[0] 是 code ，cacheInfo[1] 是 count
            String value = redisCacheUtils.getCacheObject(codeKey);
            if (value == null) {
                return Result.error(HttpStatus.HTTP_VERIFY_FAIL.getCode(), "验证码已失效，请重新发送");
            }
            String[] cacheInfo = WaveStrUtils.strSplitToArr(value, ",");
            // 4.校验验证码
            if (!cacheInfo[0].equals(code)) {
                // 4.1 验证码不正确，count++
                int count = Integer.parseInt(cacheInfo[1]);
                count++;

                if (count >= BasicConstants.LOGOFF_MAX_VERIFY_CODE_COUNT) {
                    // 次数大于等于设置的最大次数，验证码失效，移除 redis中 的验证码缓存
                    redisCacheUtils.deleteObject(codeKey);
                    return Result.error(HttpStatus.HTTP_VERIFY_FAIL.getCode(), "多次校验失败，验证码已失效，请重新发送");
                } else {
                    // 如果不是，就修改缓存中的信息
                    Long expire = redisCacheUtils.getExpire(codeKey);
                    if (expire > 0) {
                        redisCacheUtils.setCacheObject(codeKey, cacheInfo[0] + "," + count, expire);
                    }
                    return Result.error(HttpStatus.HTTP_VERIFY_FAIL.getCode(), "验证码错误");
                }
            }

            // 4.2 校验成功，继续往后走

            // 5.删除验证码缓存
            redisCacheUtils.deleteObject(codeKey);

            // 6.添加信息到 logoff 表
            Logoff logoff = new Logoff();
            // 用户id
            logoff.setUserId(userId);
            // 手机号
            logoff.setPhone(userMapper.selectPhoneById(userId));
            // 冻结开始的时间
            logoff.setLogoffTime(LocalDateTime.now());
            // 截止时间
            logoff.setEndTime(LocalDateTime.now().plusDays(BasicConstants.LOGOFF_FROZEN_DAY));
            // 注销原因
            logoff.setReason(reason);
            // 保存信息到数据库
            logoffService.save(logoff);

            // 7.修改 user 表的用户状态
            User user = new User();
            user.setId(userId);
            user.setStatus(1);
            userMapper.updateById(user);

            // 8.删除 RedisUser 缓存
            redisCacheUtils.deleteObject(RedisConstants.LOGIN_USER_INFO_KEY + userId);

            // 9.退出登录
            return Result.ok("账号将于七天后完成注销，七天内再次登录自动解除注销冻结");

        } finally {
            // 最后释放锁
            if (lock) {
                redisLockUtils.unlock(lockKey);
            }
        }
    }

    @Override
    public Result updatePwd(UpdatePwdVO updatePwdVO) {
        // 1.如果两个密码不一致，则返回error
        if (!updatePwdVO.getFirPassword().equals(updatePwdVO.getSecPassword())) {
            return Result.error(HttpStatus.HTTP_VERIFY_FAIL.getCode(), "两次密码不一致");
        }
        // 2.校验验证码是否正确
        String code = redisCacheUtils.getCacheObject(RedisConstants.PWD_CODE_KEY + UserHolderUtils.getUserId());
        // 2.1 不存在则返回
        if (code == null) {
            return Result.error(HttpStatus.HTTP_VERIFY_FAIL.getCode(), "验证码已失效，请重新获取");
        }
        // 2.2 存在但不一致则清除并返回（忽略大小写）
        if (!code.equalsIgnoreCase(updatePwdVO.getCode())) {
            redisCacheUtils.deleteObject(RedisConstants.PWD_CODE_KEY + UserHolderUtils.getUserId());
            return Result.error(HttpStatus.HTTP_VERIFY_FAIL.getCode(), "验证码错误，请重新获取");
        }

        // 3.验证码正确，则从缓存中移除
        redisCacheUtils.deleteObject(RedisConstants.PWD_CODE_KEY + UserHolderUtils.getUserId());
        // 4.修改密码
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, UserHolderUtils.getUserId())
                .set(User::getPassword, PasswordEncoderUtils.encode(updatePwdVO.getFirPassword()));
        this.update(wrapper);
        // 5.返回
        return Result.ok();
    }
}
