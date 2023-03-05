package com.zhulang.waveedu.note.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 笔记的文件内容表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-05
 */
@TableName("note_file_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id(自增)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 开始内容
     */
    private String beginContent;

    /**
     * 笔记内容
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
