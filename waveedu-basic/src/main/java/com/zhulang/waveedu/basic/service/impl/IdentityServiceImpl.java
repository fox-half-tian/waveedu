package com.zhulang.waveedu.basic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.basic.dao.IdentityMapper;
import com.zhulang.waveedu.basic.po.Identity;
import com.zhulang.waveedu.basic.po.UserInfo;
import com.zhulang.waveedu.basic.service.IdentityService;
import com.zhulang.waveedu.basic.vo.IdentityVO;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 飒沓流星
 * @create 2023-02-04 17:35
 */
@Service
public class IdentityServiceImpl extends ServiceImpl<IdentityMapper, Identity> implements IdentityService {
    @Resource
    private IdentityMapper identityMapper;

    @Override
    public Result addIdentity(IdentityVO identityVO) {
        // 1.判断是否是无效id
        if (RegexUtils.isSnowIdInvalid(identityVO.getUserId())) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效id");
        }
        // 2.判断type是否符合要求
        if(!(identityVO.getType()==1||identityVO.getType()==0)){
            return Result.error("type参数有误，应为0或1");
        }
        // 3.对象转换
        Identity identity = BeanUtil.copyProperties(identityVO, Identity.class);
        System.out.println(identity);
        // 4.添加用户的身份信息
        identityMapper.insert(identity);
        // 5.查询出刚刚添加的身份信息
        LambdaQueryWrapper<Identity> IdentityWrapper = new LambdaQueryWrapper<>();
        IdentityWrapper.eq(Identity::getUserId, identity.getUserId());
        IdentityWrapper.eq(Identity::getIsDeleted, 0);
        Identity R = identityMapper.selectOne(IdentityWrapper);
        // 6.返回刚刚添加的身份信息
        return Result.ok(R);
    }

    @Override
    public Result removeIdentityUserId(Long id) {

        LambdaQueryWrapper<Identity> IdentityWrapper = new LambdaQueryWrapper<>();
        IdentityWrapper.eq(Identity::getUserId, id);
        identityMapper.delete(IdentityWrapper);

        return Result.ok("删除成功");
    }

    @Override
    public Result getIdentityUserId(Long id) {
        // 1.判断是否是无效id
        if (RegexUtils.isSnowIdInvalid(id)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效id");
        }
        // 2.查出该用户的身份记录
        LambdaQueryWrapper<Identity> IdentityWrapper = new LambdaQueryWrapper<>();
        IdentityWrapper.eq(Identity::getUserId, id);
        Identity R = identityMapper.selectOne(IdentityWrapper);
        if(R == null){
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "该用户身份尚未添加");
        }
        // 3.返回结果
        return Result.ok(R);
    }
}
