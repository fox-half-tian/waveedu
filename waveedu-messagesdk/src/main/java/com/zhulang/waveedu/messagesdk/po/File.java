package com.zhulang.waveedu.messagesdk.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 笔记的文件表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-05
 */
@TableName("note_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id(自增)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0-富文本，1-markdown
     */
    private Integer type;

    /**
     * 是否为目录：0-不是目录，1-是目录
     */
    private Integer isDir;

    /**
     * 名称
     */
    private String name;

    /**
     * 父级目录
     */
    private Integer parentId;

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
