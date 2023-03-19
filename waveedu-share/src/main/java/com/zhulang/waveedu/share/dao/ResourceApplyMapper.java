package com.zhulang.waveedu.share.dao;

import com.zhulang.waveedu.share.po.ResourceApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.share.query.ApprovedResourceInfoQuery;
import com.zhulang.waveedu.share.query.NoApproveResourceInfoQuery;
import com.zhulang.waveedu.share.query.SelfApprovedInfoQuery;
import com.zhulang.waveedu.share.query.SelfResourceApplyingQuery;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 获取自己正在申请中的资源信息列表
     *
     * @param userId 用户id
     * @return 信息列表
     */
    List<SelfResourceApplyingQuery> selectSelfApplyingList(@Param("userId") Long userId);

    /**
     * 获取自己已经审批的资源信息列表
     *
     * @param userId 用户id
     * @return 信息列表
     */
    List<SelfApprovedInfoQuery> selectSelfApprovedList(@Param("userId") Long userId);
}
