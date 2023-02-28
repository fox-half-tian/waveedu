package com.zhulang.waveedu.messagesdk.test;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-02-28 22:56
 */
@SpringBootTest
public class RabbitTest {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Test
    public void sendWorkQueueMessage() {
            rabbitTemplate.convertAndSend("","work.queue", 20);
    }
}
