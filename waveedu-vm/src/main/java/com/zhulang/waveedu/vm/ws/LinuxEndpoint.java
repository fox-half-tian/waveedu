package com.zhulang.waveedu.vm.ws;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.LoginIdentityConstants;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.RedisUser;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 狐狸半面添
 * @create 2023-03-16 21:41
 */
@ServerEndpoint(value = "/linux")
@Component
public class LinuxEndpoint {

    /**
     * 线程安全的map集合
     */
    private static final Map<Long, Session> ONLINE_USERS = new ConcurrentHashMap<>();
    /**
     * 用户使用与在线虚拟机地址对应关系
     */
    private static final Map<Long, String> LINUX_URL = new ConcurrentHashMap<>();

    private final RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);

    private Long userId;


    private String linuxUrl;

    private RedisCacheUtils redisCacheUtils = SpringUtil.getBean(RedisCacheUtils.class);


    /**
     * 建立websocket连接后，被调用
     *
     * @param session 会话对象
     */
    @OnOpen
    public void onOpen(Session session) throws Exception {
        // 1.校验用户
        String token = session.getRequestParameterMap().get("token").get(0);
        Long userId = getUserId(token);
        if (userId == null) {
            session.getBasicRemote().sendText(JSON.toJSONString(Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(),"用户登录过期或无效登录，请重新登录")));
            session.close();
            return;
        }

        // 2.判断是否已经存在
        Session oldSession = ONLINE_USERS.get(userId);
        if (oldSession != null) {
            // 关闭原有连接
            oldSession.getBasicRemote().sendText(JSON.toJSONString(Result.error(HttpStatus.HTTP_TRY_AGAIN_LATER.getCode(), "您已在其它地方操作虚拟机，当前被强制下线")));
            oldSession.close();
        }

        // 3.获取新连接
        String responseJson = restTemplate
                .exchange("http://114.132.54.165:6666/vm", HttpMethod.GET, null, String.class)
                .getBody();
        JSONObject info = JSONObject.parseObject(responseJson);
        if (!"1".equals(info.getString("code"))) {
            // 如果不相等说明获取失败
            session.getBasicRemote().sendText(JSON.toJSONString(Result.error("获取失败，请稍后重试")));
            session.close();
            return;
        }
        // 4.获取linux的url
        String url = info.getString("url");

        // 5.将连接信息保存
        this.userId = userId;
        this.linuxUrl = url;
        ONLINE_USERS.put(userId, session);
        LINUX_URL.put(userId, url);

        // 6.将连接信息返回
        session.getBasicRemote().sendText(JSON.toJSONString(Result.ok(url)));
    }


    /**
     * 浏览器发送消息到服务端，该方法被调用
     * <p>
     * 张三  -->  李四
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {

    }

    /**
     * 断开 websocket 连接时被调用
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        if (userId != null) {
            Session storeSession = ONLINE_USERS.get(userId);
            if (session.equals(storeSession)) {
                ONLINE_USERS.remove(userId);
            }
            String url = LINUX_URL.get(userId);
            if (url != null && url.equals(linuxUrl)) {
                LINUX_URL.remove(userId);
                HashMap<String, Object> map = new HashMap<>(1);
                map.put("url",url);
                HttpEntity<JSONObject> entity = new HttpEntity<>(new JSONObject(map), null);
                restTemplate
                        .exchange("http://114.132.54.165:6666/del", HttpMethod.POST, entity, Result.class)
                        .getBody();
            }
        }
    }

    public Long getUserId(String token) {
        if (!StringUtils.hasText(token)) {
            // 没有 token 则放行
            return null;
        }

        // 解析token
        String decrypt = CipherUtils.decrypt(token);
        if (decrypt == null) {
            return null;
        }
        String[] info = WaveStrUtils.strSplitToArr(decrypt, "-");
        if (info.length != 3) {
            return null;
        }
        // 从redis中获取用户信息
        // info[0]是身份标识，info[1]是id，info[2]是uuid
        RedisUser redisUser;
        if (Integer.parseInt(info[0]) == LoginIdentityConstants.USER_MARK) {
            // 如果是普通用户
            redisUser = redisCacheUtils.getCacheObject(RedisConstants.LOGIN_USER_INFO_KEY + info[1]);
            if (Objects.isNull(redisUser)) {
                // redis中无该用户id的缓存信息
                return null;
            }
            if (!redisUser.getUuid().equals(info[2])) {
                // token未过期，但token值不一样 --> 在其它地方登录了该用户
                return null;
            }

            // 说明token有效且未过期

            /*
                (System.currentTimeMillis() - redisUser.getTime())/1000：上一次设置ttl距离现在的时长，这个时差肯定没有超过LOGIN_USER_INFO_TTL
                RedisConstants.LOGIN_USER_INFO_TTL - (System.currentTimeMillis() - redisUser.getTime()/1000)：距离过期还有多久
             */
            if (RedisConstants.LOGIN_USER_INFO_TTL - (System.currentTimeMillis() - redisUser.getTime()) / 1000 <= RedisConstants.LOGIN_USER_INFO_REFRESH_TTL) {
                // 距离过期90分钟不到，就刷新缓存
                redisUser.setTime(System.currentTimeMillis());
                redisCacheUtils.setCacheObject(RedisConstants.LOGIN_USER_INFO_KEY + info[1], redisUser, RedisConstants.LOGIN_USER_INFO_TTL);
            }
        } else if (Integer.parseInt(info[0]) == LoginIdentityConstants.ADMIN_MARK) {
            // 如果是管理员
            redisUser = redisCacheUtils.getCacheObject(RedisConstants.LOGIN_ADMIN_INFO_KEY + info[1]);
            if (Objects.isNull(redisUser)) {
                // redis中无该用户id的缓存信息
                return null;
            }
            if (!redisUser.getUuid().equals(info[2])) {
                // token未过期，但token值不一样 --> 在其它地方登录了该用户
                return null;
            }

            // 说明token有效且未过期

            /*
                (System.currentTimeMillis() - redisUser.getTime())/1000：上一次设置ttl距离现在的时长，这个时差肯定没有超过LOGIN_USER_INFO_TTL
                RedisConstants.LOGIN_USER_INFO_TTL - (System.currentTimeMillis() - redisUser.getTime()/1000)：距离过期还有多久
             */
            if (RedisConstants.LOGIN_ADMIN_INFO_TTL - (System.currentTimeMillis() - redisUser.getTime()) / 1000 <= RedisConstants.LOGIN_ADMIN_INFO_REFRESH_TTL) {
                // 距离过期90分钟不到，就刷新缓存
                redisUser.setTime(System.currentTimeMillis());
                redisCacheUtils.setCacheObject(RedisConstants.LOGIN_ADMIN_INFO_KEY + info[1], redisUser, RedisConstants.LOGIN_ADMIN_INFO_TTL);
            }
        } else {
            return null;
        }

        return redisUser.getId();
    }

}
