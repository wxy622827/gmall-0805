package com.atguigu.gmall.oms.dao;

import com.atguigu.gmall.oms.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author wangxueyan
 * @email wxy@atguigu.com
 * @date 2020-01-03 13:39:11
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
