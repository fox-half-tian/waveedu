package com.zhulang.waveedu.edu.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 普通作业表的题目表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-28
 */
@TableName("edu_common_homework_question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonHomeworkQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id（自增）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作业id
     */
    private Integer commonHomeworkId;

    /**
     * 题目类型：0-单选,1-多选,2-填空，3-判断,4-探究
     */
    private Integer type;

    /**
     * 问题描述
     */
    private String problemDesc;

    /**
     * 答案
     */
    private String answer;

    /**
     * 解析
     */
    private String analysis;

    /**
     * 分值
     */
    private Integer score;

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
