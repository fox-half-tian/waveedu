package com.zhulang.waveedu.share.vo;

import com.zhulang.waveedu.share.po.SiteTopic;
import com.zhulang.waveedu.share.po.SiteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 飒沓流星
 * @create 2023/3/19 15:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicAll {
    private SiteTopic topic;
    private List<TypeAll> types;
}
