package com.zhulang.waveedu.messagesdk.listener;

import com.zhulang.waveedu.common.constant.RabbitConstants;
import com.zhulang.waveedu.messagesdk.service.FileService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author 狐狸半面添
 * @create 2023-03-05 14:49
 */
@Component
public class NoteDirDelListener {
    @Resource
    private FileService fileService;

    /**
     * 基于 RabbitListener 注解创建交换机、队列的时候，如果发现没有该交换机与队列的时候会自动创建
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(RabbitConstants.NOTE_DIR_DEL_QUEUE_NAME),
            // 默认是 路由模式 Direct交换机
            exchange = @Exchange(name = RabbitConstants.NOTE_DIR_DEL_EXCHANGE_NAME),
            key = RabbitConstants.NOTE_DIR_DEL_QUEUE_ROUTING_KEY))
    public void listenerNoteDirDelQueue(HashMap<String, Object> map){
        // 这是删除的目录id
        Integer dirId = (Integer)map.get("dirId");
        // 进行递归删除
        fileService.removeDir(dirId);
    }
}
