package com.zhulang.waveedu.share.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.share.po.ResourceApply;
import com.zhulang.waveedu.share.dao.ResourceApplyMapper;
import com.zhulang.waveedu.share.po.Resources;
import com.zhulang.waveedu.share.service.ResourceApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.share.service.ResourceService;
import com.zhulang.waveedu.share.vo.ResourceApproveVO;
import com.zhulang.waveedu.share.vo.SaveResourceApplyVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 资源分享申请表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-19
 */
@Service
public class ResourceApplyServiceImpl extends ServiceImpl<ResourceApplyMapper, ResourceApply> implements ResourceApplyService {

    @Resource
    private ResourceApplyMapper resourceApplyMapper;
    @Resource
    private ResourceService resourceService;

    @Override
    public Result saveApply(SaveResourceApplyVO saveResourceApplyVO) {
        // 1.解密文件信息
        ResourceApply resourceApply;
        try {
            resourceApply = JSON.parseObject(CipherUtils.decrypt(saveResourceApplyVO.getFileInfo()), ResourceApply.class);
            if (resourceApply == null) {
                return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "错误的文件信息");
            }
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "错误的文件信息");
        }

        // 2.校验标签
        String tag = saveResourceApplyVO.getTag();
        if (StrUtil.isBlank(tag)) {
            tag = null;
        } else {
            List<String> tags = JSON.parseObject(tag, List.class);
            if (tags.size() > 4) {
                return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "最多四个标签");
            }
            for (String s : tags) {
                if (s.length() < 2 || s.length() > 12) {
                    return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "每个标签应在2-12字");
                }
            }
        }
        resourceApply.setTag(tag);
        // 3.设置标题，介绍，用户id
        resourceApply.setTitle(saveResourceApplyVO.getTitle());
        resourceApply.setIntroduce(saveResourceApplyVO.getIntroduce());
        resourceApply.setUserId(UserHolderUtils.getUserId());
        // 4.保存
        resourceApplyMapper.insert(resourceApply);
        // 5.返回
        return Result.ok(resourceApply.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result approve(ResourceApproveVO resourceApproveVO) {
        // 1.修改申请表状态
        int updateCount = resourceApplyMapper.update(null, new LambdaUpdateWrapper<ResourceApply>()
                .eq(ResourceApply::getId, resourceApproveVO.getId())
                .eq(ResourceApply::getStatus, 0)
                .set(ResourceApply::getStatus, resourceApproveVO.getStatus())
                .set(ResourceApply::getMark, resourceApproveVO.getMark())
                .set(ResourceApply::getAdminId, UserHolderUtils.getUserId())
                .set(ResourceApply::getApproveTime, LocalDateTime.now()));
        // 2.修改失败说明已被审批
        if (updateCount == 0) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "该申请已被审批");
        }
        // 3.如果是拒绝，则直接返回
        if (resourceApproveVO.getStatus()==2){
            return Result.ok();
        }
        // 4.获取申请表的资源信息记录
        ResourceApply applyInfo = resourceApplyMapper.selectById(resourceApproveVO.getId());

        // 5.设置资源表记录信息
        Resources resources = new Resources();
        resources.setTitle(applyInfo.getTitle());
        resources.setUserId(applyInfo.getUserId());
        resources.setIntroduce(applyInfo.getIntroduce());
        resources.setFilePath(applyInfo.getFilePath());
        resources.setFileType(applyInfo.getFileType());
        resources.setFileFormat(applyInfo.getFileFormat());
        resources.setFileFormatSize(applyInfo.getFileFormatSize());
        resources.setFileByteSize(applyInfo.getFileByteSize());
        resources.setTag(applyInfo.getTag());

        // 6.增加记录到资源表
        resourceService.save(resources);
        // 7.返回
        return Result.ok();
    }
}
