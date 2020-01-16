package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.wms.api.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wxy
 * @create 2020-01-14 15:37
 */
@FeignClient("wms-service")
public interface GmallWmsClient extends GmallWmsApi {

}
