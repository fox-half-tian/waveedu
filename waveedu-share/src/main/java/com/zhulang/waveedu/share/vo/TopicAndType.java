package com.zhulang.waveedu.share.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 飒沓流星
 * @create 2023/3/16 13:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicAndType {
    private Long id;
    private String name;
    private List<String> typeName;
}
