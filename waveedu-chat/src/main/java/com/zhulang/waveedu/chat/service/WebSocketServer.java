package com.zhulang.waveedu.chat.service;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.chat.Message.ErrorMessage;
import com.zhulang.waveedu.chat.Message.Message;
import com.zhulang.waveedu.chat.Message.MessageList;
import com.zhulang.waveedu.chat.Message.OnlineMessage;
import com.zhulang.waveedu.chat.dao.BasicUserMapper;
import com.zhulang.waveedu.chat.dao.ChatClassRecordMapper;
import com.zhulang.waveedu.chat.dao.EduLessonClassStuMapper;
import com.zhulang.waveedu.chat.pojo.BasicUserInfo;
import com.zhulang.waveedu.chat.pojo.ChatClassRecord;
import com.zhulang.waveedu.chat.pojo.EduLessonClassStu;
import com.zhulang.waveedu.chat.utils.SnowflakeIdWorker;
import com.zhulang.waveedu.common.util.JwtUtils;
import com.zhulang.waveedu.common.util.RedisCacheUtils;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
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
    //private Session session;
    //旧：concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。由于遍历set费时，改用map优化
    //private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    //新：使用map对象优化，便于根据sid来获取对应的WebSocket
    private static ConcurrentHashMap<String, Session> WEBSOCKET_MAP;

    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Session>> classMap = new ConcurrentHashMap<>();


    /**
     * Resource和Autowired注入的对象的时机是在容器启动的时候，而websocket是在有连接的时候才创建实例对象，此时已经过了容器初始化时间了，就不会自动注入。
     * 因此，需要在每次websocket实例化的时候手动注入一下，从spring容器中拿到RedisCacheUtils实例对象。
     */
    private final RedisCacheUtils redisCacheUtils = SpringUtil.getBean(RedisCacheUtils.class);

    private String classId;

    private String userId;


    private final ChatClassRecordMapper chatClassRecordMapper = SpringUtil.getBean(ChatClassRecordMapper.class);
    private final BasicUserMapper basicUserMapper = SpringUtil.getBean(BasicUserMapper.class);
    private final EduLessonClassStuMapper eduLessonClassStuMapper = SpringUtil.getBean(EduLessonClassStuMapper.class);

    /**
     * 连接成功后调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("class_id") String classId) throws IOException {
        if (initSessionAndCheckMessage(session, classId)) {
            return;
        }
        addSessionToMap(session, classId);
        sendHistoryMessageForInitConnect(session, classId);
        for (String key : WEBSOCKET_MAP.keySet()) {
            Session sessionForOnlineMessage = WEBSOCKET_MAP.get(key);
            sendOnlineMessage(sessionForOnlineMessage, classId);
        }
        addOnlineCount();           //在线数加1
        log.info("有新窗口开始监听:" + classId + ",当前在线人数为" + getOnlineCount());
    }

    /**
     * 发送在线的人物信息
     *
     * @param session session
     * @param classId 班级ID
     * @throws IOException io异常
     */
    private void sendOnlineMessage(Session session, String classId) throws IOException {
        List<Long> userIdByClassId = eduLessonClassStuMapper.getUserIdByClassId(Long.valueOf(classId));
        Map<String, Set<Long>> classOnline = new HashMap<>(31);
        Set<Long> userIdSet = new HashSet<>();
        for (String userId : WEBSOCKET_MAP.keySet()) {
            userIdSet.add(Long.valueOf(userId));
        }
        classOnline.put(classId, userIdSet);
        redisCacheUtils.setCacheMap("CLASS_ONLINE:", classOnline);
        Set<Long> notOnline = new HashSet<>();
        for (Long userId : userIdByClassId) {
            if (!userIdSet.contains(userId)) {
                notOnline.add(userId);
            }
        }
        OnlineMessage onlineMessage = new OnlineMessage();
        List<BasicUserInfo> onlineUser = getBaseUserInfo(userIdSet);
        onlineMessage.setOnlinePeople(onlineUser);
        List<BasicUserInfo> notOnlineUser = getBaseUserInfo(notOnline);
        onlineMessage.setNotOnlinePeople(notOnlineUser);
        onlineMessage.setMessageType(4);
        String onlineUserMessage = JSON.toJSONString(onlineMessage);
        session.getBasicRemote().sendText(onlineUserMessage);
    }

    /**
     * 加入用户信息到本地map里面
     *
     * @param session session
     * @param classId 班级ID
     */
    private void addSessionToMap(Session session, String classId) {
        BasicUserInfo basicUserInfo = basicUserMapper.getBasicUserInfo(Long.valueOf(userId)).get(0);
        String json = JSON.toJSONString(basicUserInfo);
        redisCacheUtils.setCacheObject("CHAT_BASE_MSG:" + userId, json);
        if (classMap.containsKey(classId)) {
            WEBSOCKET_MAP = classMap.get(classId);
        } else {
            WEBSOCKET_MAP = new ConcurrentHashMap<>(32);
        }
        WEBSOCKET_MAP.put(userId, session);
        classMap.put(classId, WEBSOCKET_MAP);
    }

    /**
     * 属性初始化以及检查用户信息是否合法
     *
     * @param session session
     * @param classId 班级id
     * @return 是否继续进行websocket连接
     * @throws IOException 异常
     */
    private boolean initSessionAndCheckMessage(Session session, String classId) throws IOException {
        this.classId = classId;
        String token = session.getRequestParameterMap().get("token").get(0);
        Claims claims = null;
        try {
            claims = JwtUtils.parseJWT(token);
        } catch (Exception e) {
            errorMessage(session, "Token解析错误");
            session.close();
            return true;
        }
        assert claims != null;
        this.userId = claims.getSubject();
        EduLessonClassStu eduLessonClassStuByClassIdAndUserId = eduLessonClassStuMapper.getEduLessonClassStuByClassIdAndUserId(Long.valueOf(classId), Long.valueOf(userId));
        if (eduLessonClassStuByClassIdAndUserId == null) {
            errorMessage(session, "非该班级学生，无法进入班级群聊");
            session.close();
            return true;
        }
        return false;
    }

    private List<BasicUserInfo> getBaseUserInfo(Set<Long> users) {
        List<BasicUserInfo> userInfoList = new ArrayList<>();
        for (Long userId : users) {
            String redisJson = redisCacheUtils.getCacheObject("CHAT_BASE_MSG:" + userId);
            BasicUserInfo info = null;
            if (redisJson == null) {
                info = basicUserMapper.getBasicUserInfo(userId).get(0);
            } else {
                String replace = redisJson.replace("\\", "");
                System.out.println(replace);
                try {
                     info = JSON.parseObject(replace, BasicUserInfo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            userInfoList.add(info);
        }
        return userInfoList;
    }

    /**
     * 第一次连接websocket推送历史消息给客户端
     *
     * @param session 客户端session
     * @param classId 班级ID
     * @throws IOException io异常
     */
    private void sendHistoryMessageForInitConnect(Session session, String classId) throws IOException {
        Message message = new Message();
        message.setPage(10);
        message.setChatMessage(new ChatClassRecord(Long.valueOf(classId)));
        MessageList messageList = getMessageList(message);
        Map<Long, BasicUserInfo> userInfoByUserId = new HashMap<>();
        for (ChatClassRecord chatClassRecord : messageList.getChatClassRecords()) {
            BasicUserInfo basicUserInfo;
            if (!userInfoByUserId.containsKey(chatClassRecord.getUserId())) {
                basicUserInfo = basicUserMapper.getBasicUserInfo(chatClassRecord.getUserId()).get(0);
                if (basicUserInfo == null) {
                    continue;
                }
                userInfoByUserId.put(chatClassRecord.getUserId(), basicUserInfo);
            } else {
                basicUserInfo = userInfoByUserId.get(chatClassRecord.getUserId());
            }
            chatClassRecord.setName(basicUserInfo.getName());
            chatClassRecord.setIcon(basicUserInfo.getIcon());
        }
        String msg = JSON.toJSONString(messageList);
        session.getBasicRemote().sendText(msg);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        WEBSOCKET_MAP = classMap.get(classId);
        if (WEBSOCKET_MAP != null) {
            WEBSOCKET_MAP.remove(userId);
        }
    }

    /**
     * 收到客户端消息后调用的方法，根据业务要求进行处理，这里就简单地将收到的消息直接群发推送出去
     *
     * @param json 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String json, Session session) throws IOException {
        log.info("收到来自窗口" + userId + "的信息:" + json);
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 1);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = JSONObject.parseObject(json);
        } catch (JSONException e) {
            errorMessage(session, "发送的消息格式错误，无法解析！");
            session.close();
            return;
        }
        Message messageObject = JSONObject.toJavaObject(jsonObject, Message.class);
        BasicUserInfo userMsg = basicUserMapper.getBasicUserInfo(messageObject.getChatMessage().getUserId()).get(0);
        messageObject.getChatMessage().setName(userMsg.getName());
        messageObject.getChatMessage().setIcon(userMsg.getIcon());
        messageObject.getChatMessage().setId(snowflakeIdWorker.nextId());
        messageObject.getChatMessage().setCreateTime(new Timestamp(System.currentTimeMillis()));
        messageObject.getChatMessage().setUpdateTime(new Timestamp(System.currentTimeMillis()));
        switch (messageObject.getMessageType()) {
            //0-->系统消息
            case 0:
                systemMessage(messageObject, session);
                break;
            //0-->普通消息
            case 1:
                defaultMessage(messageObject, session);
                break;
            //0-->历史消息
            case 2:
                historyMessage(messageObject, session);
                break;
            default:
                break;
        }

    }

    public void systemMessage(Message message, Session session) {

    }

    public void defaultMessage(Message message, Session session) throws IOException {
        chatClassRecordMapper.insertChatMessage(message.getChatMessage());
        message.setMessageType(0);
        WEBSOCKET_MAP = classMap.get(session.getRequestParameterMap().get("class_id").get(0));
        for (String key : WEBSOCKET_MAP.keySet()) {
            String msg = JSON.toJSONString(message);
            WEBSOCKET_MAP.get(key).getBasicRemote().sendText(msg);
        }
    }

    public void historyMessage(Message message, Session session) throws IOException {
        MessageList messageList = getMessageList(message);
        WEBSOCKET_MAP = classMap.get(session.getRequestParameterMap().get("class_id").get(0));
        for (String key : WEBSOCKET_MAP.keySet()) {
            String msg = JSON.toJSONString(messageList);
            WEBSOCKET_MAP.get(key).getBasicRemote().sendText(msg);
        }
    }

    /**
     * 获取历史消息
     *
     * @param message Request请求的json文本
     * @return 历史消息
     */
    @NotNull
    private MessageList getMessageList(Message message) {
        int page = message.getPage() * 10;
        List<ChatClassRecord> allByClassId = chatClassRecordMapper.getAllByClassId(message.getChatMessage().getClassId(), page);
        MessageList messageList = new MessageList();
        messageList.setMessageType(2);
        messageList.setPage(message.getPage());
        messageList.setChatClassRecords(allByClassId);
        return messageList;
    }

    public void errorMessage(Session session, String errorMsg) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessageType(3);
        errorMessage.setMsg(errorMsg);
        String msg = JSON.toJSONString(errorMessage);
        session.getBasicRemote().sendText(msg);
    }

    /**
     * 发生错误时的回调函数
     *
     * @param session ss
     * @param error   err
     */
    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        log.error("发生错误");
        errorMessage(session, "websocket错误");
    }

    /**
     * 实现服务器主动推送消息
     */
//    public void sendMessage(String message) throws IOException {
//        getBasicRemote().sendText(message);
//    }


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
