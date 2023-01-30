package com.zhulang.waveedu.basic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.basic.dao.UserInfoMapper;
import com.zhulang.waveedu.basic.po.UserInfo;
import com.zhulang.waveedu.basic.service.UserInfoService;
import com.zhulang.waveedu.basic.vo.UpdateUserInfoVO;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.RedisUser;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author 飒沓流星
 * @create 2023-01-28 17:52
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RedisCacheUtils redisCacheUtils;

    @Override
    public Result getSelfSimpleInfo() {
        // 1.获取缓存的用户信息
        RedisUser redisUser = UserHolderUtils.getRedisUser();
        // 2.装配信息
        HashMap<String, String> map = new HashMap<>(2);
        map.put("name", redisUser.getName());
        map.put("icon", redisUser.getIcon());
        // 3.返回给前端
        return Result.ok(map);
    }

    @Override
    public Result getUserInfoById(Long id) {
        // 1.判断是否是无效id
        if (RegexUtils.isSnowIdInvalid(id)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效id");
        }
        // 2.获取用户的信息：signature sex born
//        LambdaQueryWrapper<UserInfo> userInfoWrapper = new LambdaQueryWrapper<>();
//        userInfoWrapper.select(UserInfo::getSignature, UserInfo::getSex, UserInfo::getBorn)
//                .eq(UserInfo::getId, id);
//        UserInfo userInfo = userInfoMapper.selectOne(userInfoWrapper);

        // 2.获取用户的信息
        UserInfo userInfo = userInfoMapper.selectById(id);
        // 3.判断是否存在 userInfo 信息
        if (userInfo == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "该用户信息不存在");
        }
        // 4.返回信息
        return Result.ok(userInfo);
    }

    @Override
    public Result modifyUserInfo(UpdateUserInfoVO updateUserInfoVO) {
        // 1.校验 个性签名（正则有瑕疵）
        if (updateUserInfoVO.getSignature()!=null){
            String signature = updateUserInfoVO.getSignature().trim();
            if (!StringUtils.hasText(signature)){
                signature = "";
            }
            updateUserInfoVO.setSignature(signature);
        }
        // 2.对象转换
        UserInfo userInfo = BeanUtil.copyProperties(updateUserInfoVO, UserInfo.class);
        // 3.信息修改
        userInfoMapper.updateById(userInfo);
        // 4.获取缓存中的用户信息
        RedisUser redisUser = UserHolderUtils.getRedisUser();
        // 5.查看 icon 和 name 是否修改 --> 修改了则修改缓存信息
        if (userInfo.getName()!=null||userInfo.getIcon()!=null){
            if (userInfo.getName()!=null){
                redisUser.setName(userInfo.getName());
            }
            if (userInfo.getIcon()!=null){
                redisUser.setIcon(userInfo.getIcon());
            }
            redisCacheUtils.setCacheObject(RedisConstants.LOGIN_USER_INFO_KEY + redisUser.getId(), redisUser, RedisConstants.LOGIN_USER_INFO_TTL);
        }
        // 6.返回前端修改后的用户信息
        return Result.ok(userInfoMapper.selectById(userInfo.getId()));
    }
}
