package com.zhulang.waveedu.note.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-05 22:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleDirInfoQuery {
    /**
     * 目录id
     */
    private Integer id;
    /**
     * 目录名
     */
    private String name;
}
