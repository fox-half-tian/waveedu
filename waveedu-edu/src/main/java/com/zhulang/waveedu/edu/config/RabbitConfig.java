package com.zhulang.waveedu.edu.config;

import com.zhulang.waveedu.common.constant.MessageSdkSendErrorTypeConstants;
import com.zhulang.waveedu.common.constant.RabbitConstants;
import com.zhulang.waveedu.common.mapper.JacksonObjectMapper;
import com.zhulang.waveedu.edu.po.MessageSdkSendErrorLog;
import com.zhulang.waveedu.edu.service.MessageSdkSendErrorLogService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;

import static com.zhulang.waveedu.common.constant.RabbitConstants.PROGRAM_HOMEWORK_PUBLISH_DELAYED_EXCHANGE_NAME;

/**
 * @author 狐狸半面添
 * @create 2023-02-28 21:31
 */
@Configuration
@Slf4j
public class RabbitConfig implements ApplicationContextAware {
    @Resource
    private MessageSdkSendErrorLogService messageSdkSendErrorLogService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 1.从 spring 容器 中获取 RabbitTemplate 对象
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        // 2.给 RabbitTemplate 对象注册一个消息回执对象
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            /**
             * 方法的调用时机：
             * 消息到达了交换机，但是消息从交换机到队列的时候失败了
             * 这时候就会调用这个方法
             *
             * @param returnedMessage 回执信息
             */
            @Override
            public void returnedMessage(@NotNull ReturnedMessage returnedMessage) {
                // 发送时使用的交换机
                String exchange = returnedMessage.getExchange();
                // 如果是普通/编程作业延迟交换机，就不进行处理 todo 看有没有更好的解决办法
                if (exchange.equals(RabbitConstants.COMMON_HOMEWORK_PUBLISH_DELAYED_EXCHANGE_NAME)||exchange.equals(PROGRAM_HOMEWORK_PUBLISH_DELAYED_EXCHANGE_NAME)) {
                    return;
                }
                // 当前失败的消息对象
                Message message = returnedMessage.getMessage();
                // 消息失败的状态码
                int replyCode = returnedMessage.getReplyCode();
                // 发送失败的原因说明
                String replyText = returnedMessage.getReplyText();
                // 发送时使用的路由Key
                String routingKey = returnedMessage.getRoutingKey();


                log.error("发送失败的消息：{}，状态码：{}，原因是：{}，使用的交换机：{}，路由为：{}"
                        , new String(message.getBody())
                        , replyCode
                        , replyText
                        , exchange
                        , routingKey);

                // 保存到数据库
                MessageSdkSendErrorLog messageSdkSendErrorLog = new MessageSdkSendErrorLog();
                messageSdkSendErrorLog.setType(MessageSdkSendErrorTypeConstants.EXCHANGE_TO_QUEUE_SEND_ERROR);
                messageSdkSendErrorLog.setContent(new String(message.getBody()));
                messageSdkSendErrorLog.setErrorMsg(replyText);
                messageSdkSendErrorLog.setRemark("状态码：" + replyCode + "，使用的交换机：" + exchange + "，路由为：" + routingKey);
                messageSdkSendErrorLogService.save(messageSdkSendErrorLog);
            }
        });
    }

    /**
     * 使用JSON方式来做序列化和反序列化。
     */
    @Bean
    public Jackson2JsonMessageConverter rabbitMessageConverter(){
        return new Jackson2JsonMessageConverter(new JacksonObjectMapper());
    }
}
