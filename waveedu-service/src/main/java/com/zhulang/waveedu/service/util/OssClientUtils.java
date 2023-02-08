package com.zhulang.waveedu.service.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.zhulang.waveedu.common.entity.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 狐狸半面添
 * @create 2023-01-29 1:36
 */

public class OssClientUtils {
    private OSS ossClient;
    private String accessId;

    private String endpoint;

    private String bucket;

    public OssClientUtils(OSS ossClient, String accessId, String endpoint, String bucket) {
        this.ossClient = ossClient;
        this.accessId = accessId;
        this.endpoint = endpoint;
        this.bucket = bucket;
    }


    public Result policy(String dirType) {
        // 1.指定填写Host地址，格式为https://bucketname.endpoint
        String host = "https://" + bucket + "." + endpoint;

        // 2.设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        String format = new SimpleDateFormat("/yyyy/MM/").format(new Date());
        String dir = dirType + format;

        Map<String, String> respMap = null;
        try {
            // 3.指定默认超时时间是30s
            long expireTime = 30;

            // 4.得到最终的截止时间
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);

            // 5.封装签名
            PolicyConditions policyConds = new PolicyConditions();
            // 5.1 设置可上传文件的大小，这里设置为 0 - 10MB
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 10485760);
            // 5.2 设置上传文件的前缀、可忽略
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            // 5.3 对Policy签名后的字符串。
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            // 6.用户表单上传的策略（Policy），Policy为经过Base64编码过的字符串。
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);

            // 7.封装签名直传服务返回给客户端消息Body内容
            respMap = new LinkedHashMap<>();
            // 7.1 用户请求的AccessKey ID。
            respMap.put("accessid", accessId);
            // 7.2 用户表单上传的策略（Policy），Policy为经过Base64编码过的字符串。
            respMap.put("policy", encodedPolicy);
            // 7.3 对Policy签名后的字符串。
            respMap.put("signature", postSignature);
            // 7.4 限制上传的文件前缀。
            respMap.put("dir", dir);
            // 7.5 用户发送上传请求的域名。
            respMap.put("host", host);
            // 7.6 由服务器端指定的Policy过期时间，格式为Unix时间戳（自UTC时间1970年01月01号开始的秒数）。
            respMap.put("expire", String.valueOf(expireEndTime / 1000));

        } catch (Exception e) {
            return Result.error();
        }
        return Result.ok(respMap);
    }

}
