package com.zhulang.waveedu.messagesdk.controller;


import com.zhulang.waveedu.messagesdk.service.ConsumerErrorLogService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 关联消息处理的消费异常 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-01
 */
@RestController
@RequestMapping("/consumer-error-log")
public class ConsumerErrorLogController {
    @Resource
    private ConsumerErrorLogService consumerErrorLogService;
}
