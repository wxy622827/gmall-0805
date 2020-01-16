package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import java.util.List;
import lombok.Data;

/**
 * 基本属性名及值
 *
 *
 */
@Data
public class ItemGroupVO {

    private Long id;
    private String name;
    private List<ProductAttrValueEntity>  baseAttrValues;
}