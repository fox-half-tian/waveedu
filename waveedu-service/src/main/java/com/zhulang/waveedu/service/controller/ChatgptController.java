package com.zhulang.waveedu.service.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.service.entity.AskResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 狐狸半面添
 * @create 2023-03-25 13:13
 */
@RestController
@RequestMapping("/chatgpt")
public class ChatgptController {
    @Resource
    private RestTemplate restTemplate;
    @Value("${chatgpt.address}")
    private String address;

    /**
     * 向 chatgpt 询问问题
     *
     * @param object 问题
     * @return 回答
     */
    @PostMapping("/ask")
    public Result ask(@RequestBody JSONObject object) {
        String question = object.getString("question");
        if (StrUtil.isBlank(question)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "问题信息不允许为空");
        }
        if (question.length() > 1000) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "问题长度不超过1000字");
        }
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("question", question);
        HttpEntity<JSONObject> entity = new HttpEntity<>(new JSONObject(map), null);
        AskResult askResult = restTemplate
                .exchange("http://" + address + "/ask", HttpMethod.POST, entity, AskResult.class)
                .getBody();
        if (askResult != null && askResult.getCode()==200) {
            return Result.ok(askResult.getAnswer());
        } else {
            return Result.error();
        }
    }
}
