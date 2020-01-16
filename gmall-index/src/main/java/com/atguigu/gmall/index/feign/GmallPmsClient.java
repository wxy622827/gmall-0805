package com.atguigu.gmall.index.feign;

import com.atguigu.gmall.pms.api.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wxy
 * @create 2020-01-13 17:49
 */

    @FeignClient("pms-service")
    public interface GmallPmsClient extends GmallPmsApi {
    }

