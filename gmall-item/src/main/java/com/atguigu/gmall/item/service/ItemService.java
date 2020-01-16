package com.atguigu.gmall.item.service;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.item.feign.GmallPmsClient;
import com.atguigu.gmall.item.feign.GmallSmsClient;
import com.atguigu.gmall.item.feign.GmallWmsClient;
import com.atguigu.gmall.item.vo.ItemVO;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.ItemGroupVO;
import com.atguigu.gmall.sms.ItemSaleVO;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

/**
 * @author wxy
 * @create 2020-01-14 15:32
 */
@Service
public class ItemService {

    @Resource
    private GmallPmsClient pmsClient;

    @Resource
    private GmallSmsClient smsClient;

    @Resource
    private GmallWmsClient wmsClient;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    public ItemVO queryItemVO(Long skuId) {
        ItemVO itemVO = new ItemVO();
        itemVO.setSkuId(skuId);
        //根据skuId查询sku
        CompletableFuture<SkuInfoEntity> skuCompletableFuture = CompletableFuture.supplyAsync(() -> {
            Resp<SkuInfoEntity> skuInfoEntityResp = this.pmsClient.querySkuById(skuId);
            SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();
            if (skuInfoEntity == null) {
                return null;
            }
            itemVO.setWeight(skuInfoEntity.getWeight());
            itemVO.setSkuTitle(skuInfoEntity.getSkuTitle());
            itemVO.setSkuSubtitle(skuInfoEntity.getSkuSubtitle());
            itemVO.setPrice(skuInfoEntity.getPrice());
            return skuInfoEntity;
        },threadPoolExecutor);
        CompletableFuture<Void> categroyCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuInfoEntity -> {
            //根据sku的categoryId查询分类
            Resp<CategoryEntity> categoryEntityResp = this.pmsClient.queryCategoryById(skuInfoEntity.getCatalogId());
            CategoryEntity categoryEntity = categoryEntityResp.getData();
            if (categoryEntity != null) {
                itemVO.setCategoryId(categoryEntity.getCatId());
                itemVO.setCategoryName(categoryEntity.getName());
            }
        },threadPoolExecutor);

        CompletableFuture<Void> brandCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuInfoEntity -> {
            //根据sku中的brandId查询品牌

            Resp<BrandEntity> brandEntityResp = this.pmsClient.queryBrandById(skuInfoEntity.getBrandId());
            BrandEntity brandEntity = brandEntityResp.getData();
            if (brandEntity != null) {
                itemVO.setBrandId(brandEntity.getBrandId());
                itemVO.setBrandName(brandEntity.getName());
            }
        },threadPoolExecutor);

        CompletableFuture<Void> spuCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuInfoEntity -> {
            //根据sku中的spuId
            Resp<SpuInfoEntity> spuInfoEntityResp = this.pmsClient.querySpuById(skuInfoEntity.getSpuId());
            SpuInfoEntity spuInfoEntity = spuInfoEntityResp.getData();
            if (spuInfoEntity != null) {
                itemVO.setSpuId(spuInfoEntity.getId());
                itemVO.setSpuName(spuInfoEntity.getSpuName());

            }
        },threadPoolExecutor);
        CompletableFuture<Void> imageCompletableFuture = CompletableFuture.runAsync(() -> {
            //根据skuId查询图片
            Resp<List<SkuImagesEntity>> imagesResp = this.pmsClient.queryImagesBySkuId(skuId);
            List<SkuImagesEntity> skuImagesEntities = imagesResp.getData();
            itemVO.setImages(skuImagesEntities);
        },threadPoolExecutor);

        CompletableFuture<Void> storeCompletableFuture = CompletableFuture.runAsync(() -> {
            //根据skuId查询库存
            Resp<List<WareSkuEntity>> wareSkuResp = this.wmsClient.queryWareSkuBySkuId(skuId);
            List<WareSkuEntity> wareSkuEntities = wareSkuResp.getData();
            if (!CollectionUtils.isEmpty(wareSkuEntities)) {
                itemVO.setStore(wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() > 0));
            }
        },threadPoolExecutor);


        CompletableFuture<Void> saleCompletableFuture = CompletableFuture.runAsync(() -> {
            //根据skuId查询营销信息：积分  打折  满减
            Resp<List<ItemSaleVO>> itemSaleResp = this.smsClient.queryItemSaleVOBySkuId(skuId);
            List<ItemSaleVO> itemSaleVOS = itemSaleResp.getData();
            itemVO.setSales(itemSaleVOS);
        },threadPoolExecutor);


        //根据spuId查询描述信息
        CompletableFuture<Void> descCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuInfoEntity -> {
            Resp<SpuInfoDescEntity> spuInfoDescEntityResp = this.pmsClient.querySpuDescBySpuId(skuInfoEntity.getSpuId());
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescEntityResp.getData();
            if (spuInfoDescEntity != null && StringUtils.isNotBlank(spuInfoDescEntity.getDecript())) {
                itemVO.setDesc(Arrays.asList(StringUtils.split(spuInfoDescEntity.getDecript(), ",")));

            }
        },threadPoolExecutor);


        //根据sku的categoryId查询分组
        //2.遍历组到中间表中查询每个组的规格参数id
        //3.根据spuId和 attrId查询参数及值
        CompletableFuture<Void> groupCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuInfoEntity -> {
            Resp<List<ItemGroupVO>> groupResp = this.pmsClient.queryItemGroupVOsByCidAndSpuId(skuInfoEntity.getCatalogId(), skuInfoEntity.getSpuId());
            List<ItemGroupVO> itemGroupVOS = groupResp.getData();
            itemVO.setGroupVOS(itemGroupVOS);
        },threadPoolExecutor);


        //1.根据sku中的spuId 查询skus
        //2 根据skus获取skuids
        //3 根据skuids查询销售属性
        CompletableFuture<Void> attrCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuInfoEntity -> {
            Resp<List<SkuSaleAttrValueEntity>> skuSaleAttrValueResp = this.pmsClient.querySaleAttrValueBySpuId(skuInfoEntity.getSpuId());
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = skuSaleAttrValueResp.getData();
            itemVO.setSaleAttrValues(skuSaleAttrValueEntities);
        },threadPoolExecutor);
        CompletableFuture.allOf(categroyCompletableFuture,brandCompletableFuture,spuCompletableFuture,imageCompletableFuture,storeCompletableFuture,saleCompletableFuture


        ,descCompletableFuture,groupCompletableFuture,attrCompletableFuture).join();

        return itemVO;
    }

    public static void main(String[] args) {
        //开启另一个子任务，不需要获取这个任务的返回值
        CompletableFuture.runAsync(()->{
            System.out.println("开启一个不带返回值的子任务");
        });
        //有返回值
        //开启另一个子任务，需要获取这个任务的返回值
        CompletableFuture.supplyAsync(()->{
            System.out.println("开启一个带返回值的子任务");
          //  int i=1/0;
            return "hello";
        }).thenApplyAsync(t->{
            System.out.println("上一个任务的返回结果："+t);
            return "thenApplyAsync";//既有上一个任务的返回值，又有自己的返回值
        }).whenCompleteAsync((t,u)->{//当完成时，异步  不只一个线程   处理正常结果集  返回值：t-上一个任务正常返回集  u-上一个任务的异常信息
            System.out.println("t:"+t);
            System .out.println("u:"+u);
        }).exceptionally(t->{ //上一个任务出现异常时，才会执行 获取上一个任务的一场接结果集
            System.out.println("t:"+t);
            return "exceptionaly";
        }).handleAsync((t,u)->{
            System.out.println("handel  t:"+t);
            System .out.println("handel u:"+u);
            return "handel";
        });
    }
}
