package com.zhulang.waveedu.basic.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.basic.po.Identity;
import com.zhulang.waveedu.basic.vo.IdentityVO;
import com.zhulang.waveedu.common.entity.Result;

/**
 * @author 飒沓流星
 * @create 2023-02-04 17:35
 */
public interface IdentityService extends IService<Identity> {
    /**
     * 给该用户添加身份
     * @return 添加结果
     */
    Result addIdentity(IdentityVO identityVO);

    /**
     * 删除该用户身份
     * @return 删除结果
     */
    Result removeIdentityUserId(Long id);

    /**
     * 通过id查询用户的身份信息
     * @param id
     * @return 查询结果
     */
    Result getIdentityUserId(Long id);
}
