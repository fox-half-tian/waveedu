package com.zhulang.waveedu.common.exception;

import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 1. @ResponseBody 以json格式返回数据
 * 2. @ControllerAdvice 表示接管全局异常
 *
 * @author 狐狸半面添
 * @create 2023-01-18 17:28
 */
@ResponseBody
@ControllerAdvice
public class WaveEduExceptionHandler {
    /**
     * 1. 抛出了数据校验异常，就由handleValidException()处理
     * 2. 数据校验异常就是 MethodArgumentNotValidException.class
     * 3. 只返回第一条校验失败的信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidException(MethodArgumentNotValidException e){
        return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }

    /**
     * 编写方法，处理没有精确匹配到的异常/错误
     * 返回一个统一的信息，方便前端处理
     * Throwable.class 最顶级的异常的父类，因此可以处理各种异常
     */
    @ExceptionHandler(Throwable.class)
    public Result handleException(Exception e){
        // 项目完成后会将 e.getMessage() 剔除，只在开发阶段使用
        return Result.error(e.getMessage());
    }
}
