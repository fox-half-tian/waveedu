package com.zhulang.waveedu.messagesdk.listener;

import com.alibaba.fastjson.JSON;
import com.zhulang.waveedu.messagesdk.po.ConsumerErrorLog;
import com.zhulang.waveedu.messagesdk.service.ConsumerErrorLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 狐狸半面添
 * @create 2023-03-01 22:14
 */
@Component
@Slf4j
public class ErrorListener {
    @Resource
    private ConsumerErrorLogService consumerErrorLogService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "error.queue", durable = "true"),
            exchange = @Exchange(name = "error.exchange", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "error.key"
    ))
    public void listenerErrorQueue(Map<String,Object> message) {
        String content = JSON.toJSONString(message);
        log.error("错误的消息队列的消息：{}", content);
        // 对于消费重试失败的消息，存储到一个专门存放消费失败的表里存储
        ConsumerErrorLog consumerErrorLog = new ConsumerErrorLog();
        consumerErrorLog.setContent(content);
        consumerErrorLogService.save(consumerErrorLog);
    }
}
