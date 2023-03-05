package com.zhulang.waveedu.note.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-05 18:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileContentQuery {
    /**
     * 文件内容
     */
    private String content;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 文件类型
     */
    private Integer type;
    /**
     * 文件名
     */
    private String name;
}
