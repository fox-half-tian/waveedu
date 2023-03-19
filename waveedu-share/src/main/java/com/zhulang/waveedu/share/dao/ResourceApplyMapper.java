package com.zhulang.waveedu.share.dao;

import com.zhulang.waveedu.share.po.ResourceApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.share.query.ApprovedResourceInfoQuery;
import com.zhulang.waveedu.share.query.NoApproveResourceInfoQuery;

import java.util.List;

/**
 * <p>
 * 资源分享申请表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-19
 */
public interface ResourceApplyMapper extends BaseMapper<ResourceApply> {

    /**
     * 获取所有未审批的资源信息
     *
     * @return 信息列表
     */
    List<NoApproveResourceInfoQuery> selectNoApproveInfoList();

    /**
     * 管理员获取已经审批的信息列表
     *
     * @return 信息列表
     */
    List<ApprovedResourceInfoQuery> selectApprovedInfoList();
}
