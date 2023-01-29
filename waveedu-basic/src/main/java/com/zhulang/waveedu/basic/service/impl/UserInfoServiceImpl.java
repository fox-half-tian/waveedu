package com.zhulang.waveedu.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.basic.dao.UserInfoMapper;
import com.zhulang.waveedu.basic.po.UserInfo;
import com.zhulang.waveedu.basic.service.UserInfoService;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 飒沓流星
 * @create 2023-01-28 17:52
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;



    @Override
    public Result GetUserInfoById(Long id) {
        try {
            LambdaQueryWrapper<UserInfo> userInfoWrapper = new LambdaQueryWrapper<>();
            userInfoWrapper.eq(UserInfo::getId, id);
            UserInfo userInfo = userInfoMapper.selectOne(userInfoWrapper);
            if(userInfo == null) return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode()," 参数校验失败，修改失败 ");
            return Result.ok(userInfo);
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode()," 参数校验失败，修改失败 ");
        }

    }

    @Override
    public Result modifyUserInfoByUserInfo(UserInfo userInfo) {
        int i = userInfoMapper.updateById(userInfo);
        if(i!=1){
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode()," 参数校验失败，修改失败 ");
        }
        return Result.ok("修改成功");
    }

    @Override
    public Result getIdByName(String name) {
        try {
            LambdaQueryWrapper<UserInfo> userInfoWrapper = new LambdaQueryWrapper<>();
            userInfoWrapper.eq(UserInfo::getName, name);
            UserInfo userInfo = userInfoMapper.selectOne(userInfoWrapper);
            if(userInfo == null) return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode()," 参数校验失败，修改失败 ");
            return Result.ok(userInfo.getId());
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode()," 参数校验失败，获取失败 ");
        }
    }


}
