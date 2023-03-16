package com.zhulang.waveedu.basic.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.basic.constant.BasicConstants;
import com.zhulang.waveedu.basic.po.Admin;
import com.zhulang.waveedu.basic.dao.AdminMapper;
import com.zhulang.waveedu.basic.query.AdminIdAndPasswordAndStatusQuery;
import com.zhulang.waveedu.basic.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.basic.vo.AdminModifyInfoVO;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.LoginIdentityConstants;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.RedisUser;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.*;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

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
            lock = redisLockUtils.tryLock(lockKey, RedisConstants.LOCK_LOGIN_ADMIN_CODE_TTL);
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
            userInfoWrapper.select(Admin::getId, Admin::getNickName, Admin::getIcon, Admin::getRole)
                    .eq(Admin::getId, adminQuery.getId());
            Admin admin = this.getOne(userInfoWrapper);
            // 11.移除缓存中的次数统计
            redisCacheUtils.deleteObject(usernameKey);

            // 12.缓存用户信息，携带 token 返回成功，以后请求时前端需要携带 token，放在请求头中
            String token = saveRedisInfo(admin);
            HashMap<String, Object> resultMap = new HashMap<>(2);
            resultMap.put("token", token);
            resultMap.put("role", admin.getRole());
            return Result.ok(resultMap);
        } finally {
            // 13.最后释放锁
            if (lock) {
                redisLockUtils.unlock(lockKey);
            }
        }

    }

    @Override
    public Result saveAdmin() {
        // 1.设置信息
        Admin admin = new Admin();
        // 1.1 用户名
        admin.setUsername(RandomUtil.randomString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 16));
        // 1.2 密码
        admin.setPassword(RandomUtil.randomString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 24));
        // 1.3 昵称
        admin.setNickName("逐浪管理员" + RandomUtil.randomString(5));
        // 1.4 头像
        admin.setIcon(BasicConstants.DEFAULT_ADMIN_ICON);
        // 1.5 权限，普通管理员
        admin.setRole(1);
        // 1.6 默认禁用
        admin.setStatus(0);
        // 2.插入信息
        adminMapper.insert(admin);
        // 3.返回id
        return Result.ok(admin.getId());
    }

    @Override
    public Result getSelfSimpleInfo() {
        RedisUser redisUser = UserHolderUtils.getRedisUser();
        HashMap<String, String> resultMap = new HashMap<>(2);
        resultMap.put("nickName", redisUser.getName());
        resultMap.put("icon", redisUser.getIcon());
        return Result.ok(resultMap);
    }

    @Override
    public Result modifySelfInfo(AdminModifyInfoVO adminModifyInfoVO) {
        // 判断昵称格式
        String nickName = adminModifyInfoVO.getNickName();
        if (nickName != null) {
            nickName = nickName.trim();
            if (StrUtil.isBlank(nickName)) {
                return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效昵称");
            }
        }
        // 设置数据库信息与缓存信息
        RedisUser redisUser = UserHolderUtils.getRedisUser();
        Admin admin = new Admin();
        if (nickName != null) {
            admin.setNickName(nickName);
            redisUser.setName(nickName);
        }
        if (adminModifyInfoVO.getIcon() != null) {
            admin.setIcon(adminModifyInfoVO.getIcon());
            redisUser.setIcon(adminModifyInfoVO.getIcon());
        }
        // 修改数据库信息与缓存信息
        admin.setId(redisUser.getId());
        adminMapper.updateById(admin);
        redisCacheUtils.setCacheObject(RedisConstants.LOGIN_ADMIN_INFO_KEY + redisUser.getId(), redisUser, RedisConstants.LOGIN_ADMIN_INFO_TTL);
        // 返回
        return Result.ok();
    }

    @Override
    public Result switchStatus(Long adminId) {
        if (RegexUtils.isSnowIdInvalid(adminId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "管理员Id格式错误");
        }
        // 1.获取当前状态
        Integer status = adminMapper.getStatusByAdminId(adminId);
        if (status == null) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "该管理员信息不存在");
        }
        if (status == 0) {
            // 修改为启用状态
            adminMapper.updateStatusByAdmin(adminId, 1);
            return Result.ok("on");
        } else {
            // 修改为禁用状态
            adminMapper.updateStatusByAdmin(adminId, 0);
            // 缓存中删除信息
            redisCacheUtils.deleteObject(RedisConstants.LOGIN_ADMIN_INFO_KEY + adminId);
            return Result.ok("off");
        }
    }

    @Override
    public Result getAllCommonAdminInfoList() {
        return Result.ok(adminMapper.selectAllCommonAdminInfoList());
    }

    @Override
    public Result removeAdminAccount(Long adminId) {
        // 1.校验Id
        if (adminId.longValue() == UserHolderUtils.getUserId()) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "不允许删除超级管理员");
        }
        // 2.数据库中删除
        int updateCount = adminMapper.deleteById(adminId);
        if (updateCount == 0) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "管理员不存在");
        }
        // 3.缓存中删除信息
        redisCacheUtils.deleteObject(RedisConstants.LOGIN_ADMIN_INFO_KEY + adminId);
        // 4.返回
        return Result.ok();

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
