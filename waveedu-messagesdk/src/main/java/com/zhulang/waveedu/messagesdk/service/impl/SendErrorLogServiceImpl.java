package com.zhulang.waveedu.messagesdk.service.impl;

import com.zhulang.waveedu.messagesdk.po.SendErrorLog;
import com.zhulang.waveedu.messagesdk.dao.SendErrorLogMapper;
import com.zhulang.waveedu.messagesdk.service.SendErrorLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 关联消息处理的发送异常 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-28
 */
@Service
public class SendErrorLogServiceImpl extends ServiceImpl<SendErrorLogMapper, SendErrorLog> implements SendErrorLogService {

}
