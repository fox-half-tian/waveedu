package com.zhulang.waveedu.note.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-05 18:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleFileInfoQuery {
    /**
     * 文件id
     */
    private Integer id;
    /**
     * 文件名
     */
    private String name;
    /**
     * 是否为目录
     */
    private Integer isDir;
    /**
     * 文件类型
     */
    private Integer type;
}
