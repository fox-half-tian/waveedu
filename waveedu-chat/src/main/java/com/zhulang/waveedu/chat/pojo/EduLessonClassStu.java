package com.zhulang.waveedu.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author ADong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EduLessonClassStu {

  private Long id;
  private Long lessonId;
  private Long lessonClassId;
  private Long stuId;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;



}
