package com.zhulang.waveedu.sms.controller;

import cn.hutool.core.lang.UUID;
import com.google.code.kaptcha.Producer;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * 生成图片验证码
 *
 * @author 狐狸半面添
 * @create 2023-01-29 17:35
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaServiceController {
    @Resource
    private Producer producer;
    @Resource
    private RedisCacheUtils redisCacheUtils;

    /**
     * 获取修改密码的图片验证码base64编码
     * 并缓存至redis中
     *
     * @return base编码
     */
    @GetMapping("/pwd/vc.jpg")
    public Result getPwdCaptcha(){
        // 1.生成验证码字符
        String text = producer.createText();
        // 2.生成uuid
        String uuid = UUID.randomUUID().toString(true);
        // 3.生成图片
        BufferedImage bi = producer.createImage(text);
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        try {
            ImageIO.write(bi, "jpg", fos);
            // 4.缓存至 redis 中
            redisCacheUtils.setCacheObject(RedisConstants.PWD_CODE_KEY+uuid,text,RedisConstants.PWD_CODE_TTL);
            // 5.返回 uuid 与 验证码图片的base64编码
            HashMap<String, String> map = new HashMap<>(2);
            map.put("uuid",uuid);
            map.put("imgEncode",Base64.encodeBase64String(fos.toByteArray()));
            fos.flush();
            return Result.ok(map);
        }catch (Exception e){
            return Result.error();
        }finally {
            fos.close();
        }
    }
}
