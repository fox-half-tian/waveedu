package com.zhulang.waveedu.messagesdk.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author 狐狸半面添
 * @create 2023-02-28 22:56
 */
@SpringBootTest
@Slf4j
public class RabbitTest {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Test
    public void sendWorkQueueMessage() {
        // 1.创建一个消息回调对象，需要指定一个唯一的id，因为每一个消息发送成功或失败都需要回调，用于区分是哪一个消息
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 2.注册回调函数
        correlationData.getFuture().addCallback(new SuccessCallback<CorrelationData.Confirm>() {
            /**
             * 回调实际：消息发送到交换机，成功或者失败的时候都会调用
             *
             * @param result the result
             */
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // 获取本次发送消息的结果
                boolean ack = result.isAck();
                if (ack) {
                    // 表示本次消息发送成功
                    log.info("本次消息发送成功到交换机");

                } else {
                    /*
                        说明 消息没有到达交换机
                        在工作中，如果我们的消息没有到达交换机上，则需要重发，重发三次之后如果还是失败，那么这个消息需要存储到数据库中做兜底工作
                     */
                    log.error("本次消息没有到达交换机，原因是：{}", result.getReason());
                    rabbitTemplate.convertAndSend("work.exchange","work.key", "hello world", correlationData);
                }

            }
        }, new FailureCallback() {
            /**
             * 这个方法回调的时机：
             * 消息到达交换机，但是交换机的消息到队列的时候，队列没有给消息绘制的时候调用
             * 这种情况发生很少
             *
             * @param ex the failure
             */
            @Override
            public void onFailure(Throwable ex) {
                log.error("失败原因：{}" + ex.getMessage());
            }
        });

        rabbitTemplate.convertAndSend("work.exchange","work.key", "hello world", correlationData);
    }
}
