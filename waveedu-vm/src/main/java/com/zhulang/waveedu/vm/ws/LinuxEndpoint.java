package com.zhulang.waveedu.vm.ws;

import com.alibaba.fastjson.JSON;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sun.misc.MessageUtils;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
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
    private static final Map<Long, Session> onlineUsers = new ConcurrentHashMap<>();

    private RestTemplate restTemplate;

    @Autowired
    private LinuxEndpoint(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }


    /**
     * 建立websocket连接后，被调用
     *
     * @param session 会话对象
     */
    @OnOpen
    public void onOpen(Session session) {
        // 1.校验连接是否已经存在
        Long userId = UserHolderUtils.getUserId();
        onlineUsers.get(userId)

        // 1.获取连接

        //1，将session进行保存
        onlineUsers.put(userId, session);
    }

    public Set getFriends() {
        Set<String> set = onlineUsers.keySet();
        return set;
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
        //1,从onlineUsers中剔除当前用户的session对象
        String user = (String) this.httpSession.getAttribute("user");
        onlineUsers.remove(user);
        //2,通知其他所有的用户，当前用户下线了
        String message = MessageUtils.getMessage(true, null, getFriends());
        broadcastAllUsers(message);
    }
}
