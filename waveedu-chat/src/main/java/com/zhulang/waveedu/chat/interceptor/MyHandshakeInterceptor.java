package com.zhulang.waveedu.chat.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * @author 狐狸半面添
 * @create 2023-03-18 16:43
 */
public class MyHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    /**
     * @return boolean
     * @Description 握手之前，若返回false，则不建立链接
     **/
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest,
                                   ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler,
                                   Map<String, Object> attributes) throws Exception {
        //获得 accessToken ,将用户id放入socket处理器的会话(WebSocketSession)中
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) serverHttpRequest;
            attributes.put("accessToken", serverRequest.getServletRequest().getParameter("accessToken"));
        }
        // 调用父方法，继续执行逻辑
        return super.beforeHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, attributes);
    }
}
