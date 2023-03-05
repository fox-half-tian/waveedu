package com.zhulang.waveedu.note.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.note.dao.SendErrorLogMapper;
import com.zhulang.waveedu.note.po.SendErrorLog;
import com.zhulang.waveedu.note.service.SendErrorLogService;
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
