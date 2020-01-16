package com.atguigu.gmall.sms.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.sms.ItemSaleVO;
import com.atguigu.gmall.sms.dao.SkuBoundsDao;
import com.atguigu.gmall.sms.dao.SkuFullReductionDao;
import com.atguigu.gmall.sms.dao.SkuLadderDao;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.gmall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gmall.sms.entity.SkuLadderEntity;
import com.atguigu.gmall.sms.service.SkuBoundsService;
import com.atguigu.gmall.sms.vo.SaleVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;


@Service("skuBoundsService")
public class SkuBoundsServiceImpl extends ServiceImpl<SkuBoundsDao, SkuBoundsEntity> implements SkuBoundsService {

    @Resource
    private SkuLadderDao ladderDao;

    @Resource
    private SkuFullReductionDao reductionDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuBoundsEntity> page = this.page(
                new Query<SkuBoundsEntity>().getPage(params),
                new QueryWrapper<SkuBoundsEntity>()
        );

        return new PageVo(page);
    }

    @Transactional
    @Override
    public void saveSales(SaleVO saleVO) {
        // 3.1. skuBounds积分
        SkuBoundsEntity skuBoundsEntity = new SkuBoundsEntity();
        BeanUtils.copyProperties(saleVO, skuBoundsEntity);
        List<String> works = saleVO.getWork();
        skuBoundsEntity.setWork(new Integer(works.get(0)) + new Integer(works.get(1)) * 2 + new Integer(works.get(2)) * 4 + new Integer(works.get(3)) * 8);
        this.save(skuBoundsEntity);

        // 3.2. skuLadder打折
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(saleVO, skuLadderEntity);
        skuLadderEntity.setAddOther(saleVO.getLadderAddOther());
        this.ladderDao.insert(skuLadderEntity);

        // 3.3. FullReduction满减
        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(saleVO, reductionEntity);
        reductionEntity.setAddOther(saleVO.getFullAddOther());
        this.reductionDao.insert(reductionEntity);
    }

    @Override
    public List<ItemSaleVO> queryItemSaleVOBySkuId(Long skuId) {
        //根据skuId积分信息
        ArrayList<ItemSaleVO> itemSaleVOS = new ArrayList<>();
        SkuBoundsEntity skuBoundsEntity = this.getOne(new QueryWrapper<SkuBoundsEntity>().eq("sku_id", skuId));
        if(skuBoundsEntity!=null){
            ItemSaleVO itemSaleVO = new ItemSaleVO();
            itemSaleVOS.add(itemSaleVO);
            itemSaleVO.setType("积分");
            itemSaleVO.setDesc("增送"+skuBoundsEntity.getGrowBounds()+"成长积分"+skuBoundsEntity.getBuyBounds()+"购物积分");
        }
        //根据skuId打折信息
        SkuLadderEntity skuLadderEntity = this.ladderDao.selectOne(new QueryWrapper<SkuLadderEntity>().eq("sku_id", skuId));
        if(skuLadderEntity!=null){
            ItemSaleVO itemSaleVO = new ItemSaleVO();
            itemSaleVOS.add(itemSaleVO);
            itemSaleVO.setType("打折");
            itemSaleVO.setDesc("满"+skuLadderEntity.getFullCount()+"打"+skuLadderEntity.getDiscount().divide(new BigDecimal(10))+"折");
        }
        //根据skuId满减信息
        SkuFullReductionEntity skuFullReductionEntity = this.reductionDao.selectOne(new QueryWrapper<SkuFullReductionEntity>().eq("sku_id", skuId));
        if(skuFullReductionEntity!=null){
            ItemSaleVO itemSaleVO = new ItemSaleVO();
            itemSaleVOS.add(itemSaleVO);
            itemSaleVO.setType("满减");
            itemSaleVO.setDesc("满"+skuFullReductionEntity.getFullPrice()+"减"+skuFullReductionEntity.getReducePrice());

        }
        return null;
    }

}