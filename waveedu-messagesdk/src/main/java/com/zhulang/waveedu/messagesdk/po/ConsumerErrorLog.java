package com.zhulang.waveedu.messagesdk.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 关联消息处理的消费异常
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-01
 */
@TableName("messagesdk_consumer_error_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerErrorLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id(自增)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
