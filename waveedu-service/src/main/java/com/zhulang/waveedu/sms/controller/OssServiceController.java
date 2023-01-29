package com.zhulang.waveedu.sms.controller;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.sms.constant.OssConstants;
import com.zhulang.waveedu.sms.util.OssClientUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
/**
 * @author 狐狸半面添
 * @create 2023-01-29 1:30
 */
@RestController
@RequestMapping("/oss")
public class OssServiceController {
    @Resource
    private OssClientUtils ossClientUtils;

    /**
     * 获取头像的签名
     *
     * @return 签名信息
     */
    @RequestMapping("/headImage")
    public Result headImage(){
        return ossClientUtils.policy(OssConstants.HEAD_IMAGE_DIR);
    }
}
