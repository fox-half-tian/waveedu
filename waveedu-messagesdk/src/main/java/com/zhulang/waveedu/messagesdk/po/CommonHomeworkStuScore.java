package com.zhulang.waveedu.messagesdk.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 普通作业表的学生成绩表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-28
 */
@TableName("edu_common_homework_stu_score")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonHomeworkStuScore implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id(自增)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作业id
     */
    private Integer homeworkId;

    /**
     * 学生id
     */
    private Long stuId;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 满分
     */
    private Integer fullScore;

    /**
     * 是否提交
     */
    private Integer isCommit;

    /**
     * 提交时间
     */
    private LocalDateTime commitTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
