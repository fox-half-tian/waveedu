package com.zhulang.waveedu.judge.service.impl;


import cn.hutool.system.oshi.OshiUtil;
import com.zhulang.waveedu.judge.service.SystemConfigService;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author 狐狸半面添
 * @since 2023-03-14
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    public HashMap<String, Object> getSystemConfig() {
        HashMap<String, Object> result = new HashMap<>();
        // cpu核数
        int cpuCores = Runtime.getRuntime().availableProcessors();
        double cpuLoad = 100 - OshiUtil.getCpuInfo().getFree();
        // cpu使用率
        String percentCpuLoad = String.format("%.2f", cpuLoad) + "%";
        // 总内存
        double totalVirtualMemory = OshiUtil.getMemory().getTotal();
        // 空闲内存
        double freePhysicalMemorySize = OshiUtil.getMemory().getAvailable();
        double value = freePhysicalMemorySize / totalVirtualMemory;
        // 内存使用率
        String percentMemoryLoad = String.format("%.2f", (1 - value) * 100) + "%";

        result.put("cpuCores", cpuCores);
        result.put("percentCpuLoad", percentCpuLoad);
        result.put("percentMemoryLoad", percentMemoryLoad);
        return result;
    }
}