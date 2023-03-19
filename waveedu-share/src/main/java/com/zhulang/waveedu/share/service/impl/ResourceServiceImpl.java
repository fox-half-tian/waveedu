package com.zhulang.waveedu.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.share.po.Resources;
import com.zhulang.waveedu.share.dao.ResourceMapper;
import com.zhulang.waveedu.share.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 资源分析表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-19
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resources> implements ResourceService {
    @Resource
    private ResourceMapper resourceMapper;

    @Override
    public Result getSelfResourcesList() {
        return Result.ok(resourceMapper.selectList(new LambdaQueryWrapper<Resources>()
                .eq(Resources::getUserId, UserHolderUtils.getUserId())
                .orderByDesc(Resources::getCreateTime)));
    }
}
