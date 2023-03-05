package com.zhulang.waveedu.edu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-05 23:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleFileInfo {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件路径
     */
    private String filePath;
}
