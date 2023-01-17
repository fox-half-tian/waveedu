package com.zhulang.waveedu.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * Http状态码响应常量
 *
 * @author 狐狸半面添
 * @create 2023-01-17 16:46
 */
public enum HttpStatus {
    /**
     * 请求处理成功
     */
    HTTP_OK(200,"成功"),
    /**
     * 请求报文语法错误或传入后端的参数错误(格式，范围等)
     */
    HTTP_BAD_REQUEST(400,"参数有误"),
    /**
     * 需要通过HTTP认证，或认证失败
     */
    HTTP_UNAUTHORIZED(401,"认证失败"),
    /**
     * 请求资源被拒绝，权限不足
     */
    HTTP_FORBIDDEN(403,"权限不足"),
    /**
     * 无法找到请求资源（服务器无理由拒绝）
     */
    HTTP_NOT_FOUND( 404,"无法找到请求资源"),
    /**
     * 服务器故障或Web应用故障
     */
   HTTP_INTERNAL_ERROR( 500,"服务器异常"),
    /**
     * 非法操作，对服务器的恶意请求或攻击
     */
    HTTP_ILLEGAL_OPERATION(700,"非法操作"),
    /**
     * 因某些原因，服务器拒绝用户的某些请求，需要稍后再试
     */
    HTTP_TRY_AGAIN_LATER (701,"请求被拒绝，稍后再试");

    private final int code;
    private final String value;

    private HttpStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
