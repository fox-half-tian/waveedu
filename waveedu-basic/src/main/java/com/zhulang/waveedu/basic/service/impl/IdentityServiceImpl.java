package com.zhulang.waveedu.basic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.basic.dao.CollegeMapper;
import com.zhulang.waveedu.basic.dao.IdentityMapper;
import com.zhulang.waveedu.basic.po.College;
import com.zhulang.waveedu.basic.po.Identity;
import com.zhulang.waveedu.basic.po.UserInfo;
import com.zhulang.waveedu.basic.query.IdentityQuery;
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
    @Resource
    private CollegeMapper collegeMapper;

    @Override
    public Result addIdentity(IdentityVO identityVO) {
        // 1.判断是否是无效id
//        if (RegexUtils.isSnowIdInvalid(identityVO.getUserId())) {
//            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效id");
//        }
        // 2.检查是否添加过
        LambdaQueryWrapper<Identity> identityWrapper = new LambdaQueryWrapper<>();
        identityWrapper.eq(Identity::getUserId, identityVO.getUserId());
        Identity r0 = identityMapper.selectOne(identityWrapper);
        if (r0 != null) {
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "已经添加过身份");
        }
        // 3.对象转换
        Identity identity = new Identity();
        LambdaQueryWrapper<College> collegeWrapper = new LambdaQueryWrapper<>();
        collegeWrapper.eq(College::getName, identityVO.getCollegeName());
        College college = collegeMapper.selectOne(collegeWrapper);
        identity.setCollegeId(college.getId());
        identity.setNumber(identityVO.getNumber());
        identity.setType(identityVO.getType());
        identity.setUserId(identityVO.getUserId());
        identity.setCollegeName(college.getName());
        // 4.添加用户的身份信息
        try {
            identityMapper.insert(identity);
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_INFO_REFUSE.getCode(),"该身份已被其他用户注册");
        }
        // 5.查询出刚刚添加的身份信息
        Identity result = identityMapper.selectOne(identityWrapper);
        // 6.返回刚刚添加的身份信息
        IdentityQuery identityQuery = new IdentityQuery();
        identityQuery.setUserId(result.getUserId());
        identityQuery.setCollegeName(college.getName());
        identityQuery.setNumber(result.getNumber());
        identityQuery.setType(result.getType());
        identityQuery.setCollegeId(result.getCollegeId());
        return Result.ok(identityQuery);
    }

    @Override
    public Result removeIdentityUserId(Long id) {

        LambdaQueryWrapper<Identity> identityWrapper = new LambdaQueryWrapper<>();
        identityWrapper.eq(Identity::getUserId, id);
        // 逻辑删除
        int result = identityMapper.delete(identityWrapper);
        if (result == 0) {
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "当前用户已无身份，删除失败");
        }
        return Result.ok();
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
        if (R == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "该用户身份尚未添加");
        }
        IdentityQuery identityQuery = new IdentityQuery();
        identityQuery.setUserId(R.getUserId());
        // 3.查到院校名字
        LambdaQueryWrapper<College> CollegeWrapper = new LambdaQueryWrapper<>();
        CollegeWrapper.eq(College::getId, R.getCollegeId());
        College college = collegeMapper.selectOne(CollegeWrapper);
        identityQuery.setCollegeName(college.getName());
        identityQuery.setNumber(R.getNumber());
        identityQuery.setType(R.getType());
        identityQuery.setCollegeId(R.getCollegeId());
        // 3.返回结果
        return Result.ok(identityQuery);
    }

    @Override
    public Result modifyIdentity(IdentityVO identityVO) {
        // 1.判断是否是无效id
        if (RegexUtils.isSnowIdInvalid(identityVO.getUserId())) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效id");
        }
        // 2.判断type是否符合要求
        if (!(identityVO.getType() == 1 || identityVO.getType() == 0)) {
            return Result.error("type参数有误，应为0或1");
        }
        // 3.检查是否添加过
        LambdaQueryWrapper<Identity> IdentityWrapper = new LambdaQueryWrapper<>();
        IdentityWrapper.eq(Identity::getUserId, identityVO.getUserId());
        IdentityWrapper.eq(Identity::getIsDeleted, 0);
        Identity R = identityMapper.selectOne(IdentityWrapper);
        if (R == null) {
            return Result.error(HttpStatus.HTTP_ILLEGAL_OPERATION.getCode(), "请先添加身份再修改");
        }
        // 4.更新
        Identity identity = new Identity();
        identity.setType(identityVO.getType());
        identity.setNumber(identityVO.getNumber());
        LambdaQueryWrapper<College> CollegeWrapper = new LambdaQueryWrapper<>();
        CollegeWrapper.eq(College::getName, identityVO.getCollegeName());
        College college = collegeMapper.selectOne(CollegeWrapper);
        identity.setCollegeId(college.getId());
        identity.setId(R.getId());
        identityMapper.updateById(identity);
        return Result.ok("修改成功");
    }
}
