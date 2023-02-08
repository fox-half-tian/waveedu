package com.zhulang.waveedu.service.vo;

import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 分块文件上传检查封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-08 16:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChunkFileVO {
    /**
     * 需要上传的文件的md5值
     */
    @NotNull(message = "文件md5不能为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.MD5_HEX_REGEX, message = "文件md5格式错误")
    String fileMd5;
    /**
     * 该分块文件的索引
     */
    @NotNull(message = "分块文件索引不能为空")
    @Min(value = 0, message = "分块文件索引必须是大于等于0的整数")
    Integer chunkIndex;
}
