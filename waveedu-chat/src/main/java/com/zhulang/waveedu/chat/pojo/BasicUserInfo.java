package com.zhulang.waveedu.chat.pojo;


import com.google.errorprone.annotations.NoAllocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author ADong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicUserInfo {

  private Long id;
  private String name;
  private String icon;
  private String signature;
  private String sex;
  private java.sql.Date born;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;


}
