package com.zhulang.waveedu.basic.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.basic.constant.BasicConstants;
import com.zhulang.waveedu.basic.po.Admin;
import com.zhulang.waveedu.basic.dao.AdminMapper;
import com.zhulang.waveedu.basic.query.AdminIdAndPasswordAndStatusQuery;
import com.zhulang.waveedu.basic.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.LoginIdentityConstants;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.RedisUser;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import com.zhulang.waveedu.common.util.RedisLockUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private RedisCacheUtils redisCacheUtils;
    @Resource
    private RedisLockUtils redisLockUtils;

    @Override
    public Result login(String username, String password) {
        // 获取redis分布式锁
        String lockKey = RedisConstants.LOCK_LOGIN_ADMIN_KEY + username;
        String usernameKey = RedisConstants.LOGIN_ADMIN_PWD_KEY + username;
        boolean lock = false;
        try {
            // 1.拿到锁，设置TTL
            lock = redisLockUtils.tryLock(lockKey, RedisConstants.LOCK_LOGIN_USER_CODE_TTL);
            // 2.获取锁失败，直接退出
            if (!lock) {
                return Result.error(HttpStatus.HTTP_TRY_AGAIN_LATER.getCode(), HttpStatus.HTTP_TRY_AGAIN_LATER.getValue());
            }
            // 3.从 redis 中获取当前用户名的登录次数
            Integer count = redisCacheUtils.getCacheObject(usernameKey);
            if (count == null) {
                count = 0;
            }
            // 4.次数超过8次，则返回稍后再试
            if (count >= BasicConstants.LOGIN_MAX_VERIFY_PWD_COUNT) {
                return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "账号因多次输入密码错误，已冻结" + RedisConstants.LOGIN_ADMIN_PWD_LOCK_TTL / 60 + "分钟");
            }
            count++;
            // 5.从数据库中查询该用户名的用户信息
            AdminIdAndPasswordAndStatusQuery adminQuery = adminMapper.selectIdAndPasswordAndStatusByUsername(username);
            // 6.用户信息不存在则返回错误
            if (adminQuery == null) {
                return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "用户名或密码错误");
            }

            // 7.校验密码
            boolean matches = password.equals(adminQuery.getPassword());
            if (!matches) {
                // 8.校验失败的处理
                // 8.1 是否冻结用户名
                if (count >= BasicConstants.LOGIN_MAX_VERIFY_PWD_COUNT) {
                    // 冻结登录15分钟
                    redisCacheUtils.setCacheObject(usernameKey, count, RedisConstants.LOGIN_ADMIN_PWD_LOCK_TTL);
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "账号因多次输入密码错误，已冻结" + RedisConstants.LOGIN_ADMIN_PWD_LOCK_TTL / 60 + "分钟");
                }
                // 8.2 未冻结，则修改验证次数
                if (count == 1) {
                    redisCacheUtils.setCacheObject(usernameKey, count, RedisConstants.LOGIN_ADMIN_PWD_TTL);
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "手机号或密码错误");
                }
                Long expire = redisCacheUtils.getExpire(usernameKey);
                if (expire > 0) {
                    redisCacheUtils.setCacheObject(usernameKey, count, expire);
                }
                // 8.3 返回结果，当剩1-3次机会时返回剩余次数
                int surplusCount = BasicConstants.LOGIN_MAX_VERIFY_PWD_COUNT - count;
                if (surplusCount > 0 && surplusCount <= BasicConstants.LOGIN_PWD_MAX_SURPLUS_COUNT) {
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "用户名或密码错误，" + surplusCount + "次机会后冻结账号15分钟");
                } else {
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "用户名或密码错误");
                }
            }
            // 校验成功则继续往后走

            // 9.判断是否被禁用
            if (adminQuery.getStatus() == 0) {
                return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "当前管理员账号已被禁用");
            }
            // 10.查询用户信息
            LambdaQueryWrapper<Admin> userInfoWrapper = new LambdaQueryWrapper<>();
            userInfoWrapper.select(Admin::getId, Admin::getNickName, Admin::getIcon)
                    .eq(Admin::getId, adminQuery.getId());
            Admin admin = this.getOne(userInfoWrapper);
            // 11.移除缓存中的次数统计
            redisCacheUtils.deleteObject(usernameKey);

            // 12.缓存用户信息，携带 token 返回成功，以后请求时前端需要携带 token，放在请求头中
            return Result.ok(saveRedisInfo(admin));
        } finally {
            // 13.最后释放锁
            if (lock) {
                redisLockUtils.unlock(lockKey);
            }
        }

    }

    /**
     * 设置管理员信息，缓存到 redis 中
     *
     * @param admin 管理员基本信息
     * @return 生成的token
     */
    public String saveRedisInfo(Admin admin) {
        // todo 1.查询权限信息（后面再来补充）

        // 2.生成 token
        String uuid = UUID.randomUUID().toString(true);
        String token = CipherUtils.encrypt(LoginIdentityConstants.ADMIN_MARK + "-" + admin.getId() + "-" + uuid);

        // 3.设置缓存的用户信息
        RedisUser redisUser = new RedisUser();
        redisUser.setUuid(uuid);
        redisUser.setTime(System.currentTimeMillis());
        redisUser.setIcon(admin.getIcon());
        redisUser.setName(admin.getNickName());
        redisUser.setId(admin.getId());
        // todo 设置权限

        // 4.信息存储到 redis
        redisCacheUtils.setCacheObject(RedisConstants.LOGIN_ADMIN_INFO_KEY + redisUser.getId(), redisUser, RedisConstants.LOGIN_ADMIN_INFO_TTL);

        // 5.返回token
        return token;
    }
}
