package com.zhulang.waveedu.share.controller;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.po.SiteTopic;
import com.zhulang.waveedu.share.service.SiteTopicService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:22
 */
@RestController
@RequestMapping("/site-topic")
public class SiteTopicController {
    @Resource
    private SiteTopicService siteTopicService;

    @PostMapping("/add")
    public Result addSite(@Validated @RequestBody SiteTopic siteTopic) {
        return siteTopicService.addSiteTopic(siteTopic);

    }
    @DeleteMapping("/del")
    public Result removeSiteById(@Validated @RequestParam(value = "id") Long id) {
        return siteTopicService.removeSiteTopicById(id);

    }
    @GetMapping("/get")
    public Result getSite(@Validated @RequestParam(value = "id") Long id) {
        return siteTopicService.getSiteTopicById(id);

    }
    @PutMapping("/update")
    public Result updateSite(@Validated @RequestBody SiteTopic siteTopic) {
        return siteTopicService.modifySiteTopicById(siteTopic);

    }

    @GetMapping("/getall")
    public Result getSiteall() {
        return siteTopicService.getSiteTopicall();
    }
    @GetMapping("/get/type")
    public Result getType(@RequestParam(value = "id") Long id) {
        return siteTopicService.getTypeByTopicid(id);
    }
    @GetMapping("/get/type/site")
    public Result getTypeAndSite(@RequestParam(value = "id") Long id) {
        return siteTopicService.getTypeAndSiteByTopicid(id);
    }
}
