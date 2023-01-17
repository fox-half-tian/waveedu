package com.zhulang.waveedu.common.handler;

import com.alibaba.fastjson.JSON;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.zhulang.waveedu.common.constant.HttpStatus.HTTP_FORBIDDEN;

/**
 * 处理授权失败的异常
 *
 * @author 狐狸半面添
 * @create 2023-01-17 17:05
 */
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 403 请求资源被拒绝
        Result result = Result.error(HTTP_FORBIDDEN.getCode(), HTTP_FORBIDDEN.getValue());
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response, json);
    }
}
