package com.zhulang.waveedu.dispatch.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.InviteCodeConstants;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static com.zhulang.waveedu.common.constant.CommonConstants.REQUEST_HEADER_TOKEN;

/**
 * 对邀请码进行统一处理
 *
 * @author 狐狸半面添
 * @create 2023-02-17 1:22
 */
@RestController
@RequestMapping("/invite-code")
public class InviteCodeController {
    public static final String EDU_SERVICE_URL = "http://localhost:9201";
    public static final String LESSON_TCH_INVITE_CODE_CHILD_PATH =  "/edu/lesson-tch/joinTchTeam";


    @Resource
    private RestTemplate restTemplate;

    /**
     * 对邀请码进行统一校验
     *
     * @param object 邀请码
     * @return 校验结果
     */
    @PostMapping("/identity")
    public Result identity(@RequestBody JSONObject object, HttpServletRequest request) {
        String encrypt = CipherUtils.decrypt(object.getString("code").trim());
        try {
            String dispatchUrl;
            String childPath;
            String[] info = WaveStrUtils.strSplitToArr(encrypt, "-");
            switch (info[0]) {
                // 教学邀请码，转发至教育模块
                case InviteCodeConstants.LESSON_TCH_TEAM_CODE_TYPE:
                    dispatchUrl = EDU_SERVICE_URL;
                    childPath = LESSON_TCH_INVITE_CODE_CHILD_PATH;
                    break;
                default:
                    return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效邀请码");
            }
            HashMap<String, Object> map = new HashMap<>(2);
            map.put("id", info[1]);
            map.put("inviteCode", info[2]);

            HttpHeaders headers = new HttpHeaders();
            // todo 配置网关后这里将可能不再需要处理
            headers.add(REQUEST_HEADER_TOKEN, request.getHeader(REQUEST_HEADER_TOKEN));
            HttpEntity<JSONObject> entity = new HttpEntity<>(new JSONObject(map), headers);
            return restTemplate
                    .exchange(dispatchUrl + childPath, HttpMethod.POST, entity, Result.class)
                    .getBody();

        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效邀请码");
        }
    }
}
