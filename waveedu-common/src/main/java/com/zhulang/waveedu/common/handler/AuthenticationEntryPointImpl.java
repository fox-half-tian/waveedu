package com.zhulang.waveedu.common.handler;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.zhulang.waveedu.common.constant.HttpStatus.HTTP_UNAUTHORIZED;

/**
 * 处理认证失败的异常
 *
 * @author 狐狸半面添
 * @create 2023-01-17 17:13
 */
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 需要通过HTTP认证，或认证失败
        Result result =  Result.error(HTTP_UNAUTHORIZED.getCode(), "请先登录再访问");
        WebUtils.renderString(response,result);
    }
}
