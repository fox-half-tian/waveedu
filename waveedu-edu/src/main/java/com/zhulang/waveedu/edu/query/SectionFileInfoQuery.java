package com.zhulang.waveedu.edu.query;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 查询小节文件详细信息的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-16 17:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionFileInfoQuery {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 文件名，不可超过255长度
     */
    private String fileName;

    /**
     * 存储路径
     */
    private String filePath;

    /**
     * 文件格式
     */
    private String fileFormat;

    /**
     * 文件的格式大小
     */
    private String fileFormatSize;

}
