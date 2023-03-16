package com.zhulang.waveedu.share.vo;

import com.zhulang.waveedu.share.po.site;
import com.zhulang.waveedu.share.po.siteTopic;
import com.zhulang.waveedu.share.po.siteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 飒沓流星
 * @create 2023/3/16 14:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeAndSite {
    private  siteType siteType;
    private List<site> site;
}
