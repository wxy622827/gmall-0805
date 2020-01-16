package com.atguigu.gmall.item.vo;

import com.atguigu.gmall.pms.entity.SkuImagesEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.atguigu.gmall.pms.vo.ItemGroupVO;
import com.atguigu.gmall.sms.ItemSaleVO;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 * @author wxy
 * @create 2020-01-14 15:01
 */
@Data
public class ItemVO {
    //1、当前sku的基本信息
    private Long skuId;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private Long spuId;
    private  String spuName;

    private String skuTitle;
    private String skuSubtitle;
    private BigDecimal price;
    private BigDecimal weight;
    private Boolean store;//库存信息


      private List<SkuImagesEntity> images;
      //sku的所有促销信息
      private List<ItemSaleVO> sales;
      private List<SkuSaleAttrValueEntity> saleAttrValues;
      private List<String> desc;
      private List<ItemGroupVO> groupVOS;
//
//    //5、spu的所有基本属性
//    private List<ItemGroupVO> attrGroups;
//
//    //6、详情介绍
//    private SpuInfoDescEntity desc;
}
