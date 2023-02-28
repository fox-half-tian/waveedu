package com.zhulang.waveedu.messagesdk.config;

import com.zhulang.waveedu.common.constant.MessageSdkSendErrorTypeConstants;
import com.zhulang.waveedu.messagesdk.po.SendErrorLog;
import com.zhulang.waveedu.messagesdk.service.SendErrorLogService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;

import static com.zhulang.waveedu.common.constant.RabbitConstants.*;

/**
 * @author 狐狸半面添
 * @create 2023-02-28 20:58
 */
@Configuration
@Slf4j
public class RabbitConfig implements ApplicationContextAware {
    @Resource
    private SendErrorLogService sendErrorLogService;

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
                // 当前失败的消息对象
                Message message = returnedMessage.getMessage();
                // 消息失败的状态码
                int replyCode = returnedMessage.getReplyCode();
                // 发送失败的原因说明
                String replyText = returnedMessage.getReplyText();
                // 发送时使用的交换机
                String exchange = returnedMessage.getExchange();
                // 发送时使用的路由Key
                String routingKey = returnedMessage.getRoutingKey();


                log.error("发送失败的消息：{}，状态码：{}，原因是：{}，使用的交换机：{}，路由为：{}"
                        , new String(message.getBody())
                        , replyCode
                        , replyText
                        , exchange
                        , routingKey);

                // 保存到数据库
                SendErrorLog sendErrorLog = new SendErrorLog();
                sendErrorLog.setType(MessageSdkSendErrorTypeConstants.EXCHANGE_TO_QUEUE_SEND_ERROR);
                sendErrorLog.setContent(Arrays.toString(message.getBody()));
                sendErrorLog.setErrorMsg(replyText);
                sendErrorLog.setRemark("状态码：" + replyCode + "使用的交换机：" + exchange + "，路由为：" + routingKey);
                sendErrorLogService.save(sendErrorLog);
            }
        });
    }

    /**
     * 使用JSON方式来做序列化和反序列化。
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 声明普通作业定时发布延迟队列
     *
     * @return 延迟队列
     */
    @Bean
    public Queue commonHomeworkPublishDelayedQueue() {
        return new Queue(COMMON_HOMEWORK_PUBLISH_DELAYED_QUEUE_NAME);
    }

    /**
     * 声明普通作业定时发布延迟交换机
     *
     * @return 延迟交换机
     */
    @Bean
    public CustomExchange delayedExchange() {
        HashMap<String, Object> arguments = new HashMap<>(1);
        arguments.put("x-delayed-type", "direct");

        /*
            第一个参数：交换机的名称
            第二个参数：交换机的类型
            第三个参数：是否需要持久化
            第四个参数：是否需要自动删除
            第五个参数：其它的参数
         */
        return new CustomExchange(COMMON_HOMEWORK_PUBLISH_DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }

    /**
     * 普通作业定时发布
     * 将延迟队列 与 延迟交换机进行捆绑
     *
     * @param commonHomeworkPublishDelayedQueue 延迟队列
     * @param delayedExchange                   延迟交换机
     * @return 捆绑
     */
    @Bean
    public Binding commonHomeworkPublishDelayedQueueBindingExchange(@Qualifier("commonHomeworkPublishDelayedQueue") Queue commonHomeworkPublishDelayedQueue,
                                                                    @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(commonHomeworkPublishDelayedQueue).to(delayedExchange).with(COMMON_HOMEWORK_PUBLISH_EXCHANGE_ROUTING_KEY).noargs();
    }

    /**
     * 测试使用 todo 需要删除
     */
    @Bean
    public Queue workQueue() {
        return new Queue("work.queue");
    }
}
