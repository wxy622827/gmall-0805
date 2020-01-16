package com.atguigu.gmall.item.controller;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.item.vo.ItemVO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;

/**
 * @author wxy
 * @create 2020-01-14 15:26
 */
@Api(tags = "属性&属性分组关联 管理")
@RestController
@RequestMapping("item")
public class ItemController {

    @Resource
    private ItemService itemService;
     @GetMapping("{skuId}")
    public Resp<ItemVO> queryItemVO(@PathVariable("skuId")Long skuId){
         ItemVO itemVO=this.itemService.queryItemVO(skuId);
         return Resp.ok(itemVO);
    }
}
