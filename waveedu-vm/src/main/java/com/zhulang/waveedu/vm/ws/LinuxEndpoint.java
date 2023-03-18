package com.zhulang.waveedu.vm.ws;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.LoginIdentityConstants;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.RedisUser;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.CipherUtils;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import sun.misc.MessageUtils;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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

    private RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);

    private Long userId;


    @Resource
    private RedisCacheUtils redisCacheUtils;


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
            session.getBasicRemote().sendText("用户登录过期或无效登录，请重新登录");
            session.close();
            return;
        }

        // 2.判断是否已经存在
        Session oldSession = ONLINE_USERS.get(userId);
        if (oldSession != null) {
            // 关闭原有连接
            oldSession.getBasicRemote().sendText("您已在其它地方操作虚拟机，当前被强制下线");
            oldSession.close();
            return;
        }

        // 3.获取新连接
        String responseJson = restTemplate
                .exchange("http://114.132.54.165:6666/vm", HttpMethod.GET, null, String.class)
                .getBody();
        JSONObject info = JSONObject.parseObject(responseJson);
        if (!"1".equals(info.getString("code"))){
            // 如果不相等说明获取失败
            session.getBasicRemote().sendText("获取失败，请稍后重试");
        }

        onlineUsers.put(this.userId, session);
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
        try {
            //将消息推送给指定的用户
            Message msg = JSON.parseObject(message, Message.class);
            //获取 消息接收方的用户名
            String toName = msg.getToName();
            String mess = msg.getMessage();
            //获取消息接收方用户对象的session对象
            Session session = onlineUsers.get(toName);
            String user = (String) this.httpSession.getAttribute("user");
            String msg1 = MessageUtils.getMessage(false, user, mess);
            session.getBasicRemote().sendText(msg1);
        } catch (Exception e) {
            //记录日志
        }
    }

    /**
     * 断开 websocket 连接时被调用
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        if (userId != null) {
            // 1.移除在线用户
            ONLINE_USERS.remove(userId);
            // 2.移除连接
            String url = LINUX_URL.get(userId);

            LINUX_URL.remove(userId);
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

        return redisUser.getId().toString();
    }

}
