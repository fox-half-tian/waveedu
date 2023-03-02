package com.zhulang.waveedu.messagesdk.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.messagesdk.dao.ConsumerErrorLogMapper;
import com.zhulang.waveedu.messagesdk.po.ConsumerErrorLog;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 关联消息处理的消费异常 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-02
 */
@Service
public class ConsumerErrorLogServiceImpl extends ServiceImpl<ConsumerErrorLogMapper, ConsumerErrorLog> implements IService<ConsumerErrorLog> {

}
