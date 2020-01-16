package com.atguigu.gmall.auth.feign;

import com.atguigu.gmall.ums.api.GmallUmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wxy
 * @create 2020-01-15 20:41
 */
@FeignClient("ums-service")
public interface GmallUmsClient extends GmallUmsApi {

}
