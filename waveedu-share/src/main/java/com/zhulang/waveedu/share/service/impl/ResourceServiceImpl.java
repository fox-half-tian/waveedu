package com.zhulang.waveedu.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.share.Constant.ShareConstants;
import com.zhulang.waveedu.share.po.Resources;
import com.zhulang.waveedu.share.dao.ResourceMapper;
import com.zhulang.waveedu.share.query.ResourceShowInfoQuery;
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

    @Override
    public Result removeResource(Integer resourceId) {
        int delCount = resourceMapper.delete(new LambdaQueryWrapper<Resources>()
                .eq(Resources::getId, resourceId)
                .eq(Resources::getUserId, UserHolderUtils.getUserId()));
        return delCount != 0 ? Result.ok() : Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "资源不存在或权限不足");
    }

    @Override
    public Result getResourceInfo(Integer resourceId) {
        ResourceShowInfoQuery resourceInfo = resourceMapper.selectResourceInfo(resourceId);
        if (resourceInfo == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "资源不存在");
        }
        return Result.ok(resourceInfo);
    }

    @Override
    public Result getResourceInfoList(Integer resourceId) {
        if (resourceId == null || resourceId <= 0) {
            resourceId = Integer.MAX_VALUE;
        }
        return Result.ok(resourceMapper.selectResourceInfoList(resourceId, ShareConstants.RESOURCE_QUERY_DEFAULT_LIMIT_NUM));
    }

    @Override
    public Result download(Integer resourceId) {
        // 1.校验格式
        if (resourceId == null || resourceId < 1000) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "资源id格式错误");
        }
        // 2.获取资源路径
        String filePath = resourceMapper.selectFilePathById(resourceId);
        if (filePath == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "资源文件不存在");
        }
        // 3.下载次数 +1
        resourceMapper.updateDownloadCountForOne(resourceId);
        // 4.返回资源路径
        return Result.ok(filePath);
    }


}
