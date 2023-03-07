package com.zhulang.waveedu.chat.service;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.util.JwtUtils;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ADong
 * @date 2023年3月7日 02:07:59
 */
@ServerEndpoint(value = "/ws/{class_id}")
@Component
public class WebSocketServer {

    private final static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //旧：concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。由于遍历set费时，改用map优化
    //private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    //新：使用map对象优化，便于根据sid来获取对应的WebSocket
    private static ConcurrentHashMap<String, WebSocketServer> websocketMap = new ConcurrentHashMap<>();
    //接收用户的sid，指定需要推送的用户
    //private String sid;

    // @Resource和@Autowired注入的对象的时机是在容器启动的时候，而websocket是在有连接的时候才创建实例对象，此时已经过了容器初始化时间了，就不会自动注入。
    // 因此，需要在每次websocket实例化的时候手动注入一下，从spring容器中拿到RedisCacheUtils实例对象。
    private RedisCacheUtils redisCacheUtils = SpringUtil.getBean(RedisCacheUtils.class);

    private String classId;

    private String userId;

    /**
     * 连接成功后调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("class_id") String classId) {
        this.classId = classId;
        this.session = session;
        String token = session.getRequestParameterMap().get("token").get(0);
        Claims claims = null;
        try {
            claims = JwtUtils.parseJWT(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String userId = claims.getSubject();
        this.userId = userId;
        //webSocketSet.add(this);     //加入set中
        Map<String, String> classMsg = redisCacheUtils.getCacheMap(RedisConstants.CHAT_CLASS_INFO + classId);
        if (classMsg == null) {
            System.out.println("------------------");
        }
        JSONObject sessionJson = JSONObject.parseObject(JSONObject.toJSONString(session));
        classMsg.put(userId, sessionJson.toJSONString());
        redisCacheUtils.setCacheMap(RedisConstants.CHAT_CLASS_INFO + classId, classMsg);
        addOnlineCount();           //在线数加1
        log.info("有新窗口开始监听:" + classId + ",当前在线人数为" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        Map<String, String> classMsg = redisCacheUtils.getCacheMap(RedisConstants.CHAT_CLASS_INFO + classId);
        classMsg.remove(userId);
        redisCacheUtils.setCacheMap(RedisConstants.CHAT_CLASS_INFO + classId, classMsg);
    }

    /**
     * 收到客户端消息后调用的方法，根据业务要求进行处理，这里就简单地将收到的消息直接群发推送出去
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("收到来自窗口" + userId + "的信息:" + message);
        if (StringUtils.isNotBlank(message)) {
            Map<String, String> classMsg = redisCacheUtils.getCacheMap(RedisConstants.CHAT_CLASS_INFO + classId);
            for (String sessionJson : classMsg.keySet()) {
                Session session1 = JSON.parseObject(sessionJson, Session.class);
                session1.getBasicRemote().sendText(message);
            }
        }
    }

    /**
     * 发生错误时的回调函数
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送消息
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息（用set会方便些）
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
//        log.info("推送消息到窗口" + sid + "，推送内容:" + message);
//        if (StringUtils.isNotBlank(message)) {
//            for (WebSocketServer server : websocketMap.values()) {
//                try {
//                    // sid为null时群发，不为null则只发一个
//                    if (sid == null) {
//                        server.sendMessage(message);
//                    } else if (server.sid.equals(sid)) {
//                        server.sendMessage(message);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    continue;
//                }
//            }
//        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
