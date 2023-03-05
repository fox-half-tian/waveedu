package com.zhulang.waveedu.messagesdk.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-05 15:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileIdAndIsDirQuery {
    /**
     * 文件id
     */
    private Integer id;
    /**
     * 0-非目录，1-目录
     */
    private Integer isDir;
}
