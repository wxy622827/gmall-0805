package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.sms.api.GmallSmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wxy
 * @create 2020-01-14 15:37
 */
@FeignClient("sms-service")
public interface GmallSmsClient extends GmallSmsApi {
}
