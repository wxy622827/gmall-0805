package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.pms.api.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wxy
 * @create 2020-01-14 15:37
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {

}
