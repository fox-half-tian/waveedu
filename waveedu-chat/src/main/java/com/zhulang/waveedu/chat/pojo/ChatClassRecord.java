package com.zhulang.waveedu.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author ADong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatClassRecord {
  /**
   * 雪花算法，聊天记录ID
   */
  private Long id;
  /**
   * 班级ID
   */
  private Long classId;
  /**
   * 用户ID
   */
  private Long userId;
  /**
   * 发送的用户昵称
   */
  private String name;
  /**
   * 发送消息的用户头像
   */
  private String icon;
  /**
   * 消息类型(1-文本，2-图片，3-文件)
   */
  private Integer type;
  /**
   * 类型名称，文本和图片都是匿名，文件的类型名称是文件的名称
   */
  private String typeName;
  /**
   * 消息内容
   */
  private String content;
  /**
   * 记录创建时间
   */
  private java.sql.Timestamp createTime;
  /**
   * 记录更新时间
   */
  private java.sql.Timestamp updateTime;

  public ChatClassRecord(Long classId) {
    this.classId = classId;
  }
}
