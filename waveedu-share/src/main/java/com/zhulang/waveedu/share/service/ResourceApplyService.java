package com.zhulang.waveedu.share.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.po.ResourceApply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.share.vo.ResourceApproveVO;
import com.zhulang.waveedu.share.vo.SaveResourceApplyVO;

/**
 * <p>
 * 资源分享申请表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-19
 */
public interface ResourceApplyService extends IService<ResourceApply> {

    /**
     * 申请资源
     *
     * @param saveResourceApplyVO 资源信息
     * @return 申请id
     */
    Result saveApply(SaveResourceApplyVO saveResourceApplyVO);

    /**
     * 管理员审批
     *
     * @param resourceApproveVO 审批信息
     * @return 审批结果
     */
    Result approve(ResourceApproveVO resourceApproveVO);

    /**
     * 管理员获取未审批的信息列表
     *
     * @return 信息列表
     */
    Result getNoApproveList();

    /**
     * 管理员获取已经审批的信息列表
     *
     * @return 信息列表
     */
    Result getApprovedList();
}
