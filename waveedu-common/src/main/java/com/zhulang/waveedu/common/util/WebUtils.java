package com.zhulang.waveedu.common.util;

import com.alibaba.fastjson.JSON;
import com.zhulang.waveedu.common.entity.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 17:11
 */
public class WebUtils
{
    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @return null
     */
    public static String renderString(HttpServletResponse response, Result result) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSON.toJSONString(result));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
