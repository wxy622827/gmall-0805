package com.atguigu.gmall.sms;

import lombok.Data;

@Data
public class ItemSaleVO {

    // 0-优惠券    1-满减    2-阶梯
    private String type;//积分，打折，满减

    private String desc;//促销信息/优惠券的名字

}