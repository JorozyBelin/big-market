package org.example.domain.activity.service.armory;

import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.types.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ActivityArmoryDispatch implements IActivityDispatch, IActivityArmory {
    @Autowired
    private IActivityRepository activityRepository;
    @Override
    public boolean assembleActivity(Long sku) {
        // 1. 查询活动商品信息，存入到缓存
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);
        // 2. 缓存活动商品库存信息
        cacheActivitySkuStockCount(activitySkuEntity.getSku(), activitySkuEntity.getStockCount());
        // 3. 缓存活动信息
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        // 4. 缓存活动库存信息
        ActivityCountEntity activityCountEntity = activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        return true;
    }

    private void cacheActivitySkuStockCount(Long sku, Integer stockCount) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        activityRepository.cacheActivitySkuStockCount(sku,cacheKey, stockCount);
    }

    @Override
    public boolean subtractionActivitySkuStock(Long sku, Date endDateTime) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        return activityRepository.subtractionActivitySkuStock(sku,cacheKey, endDateTime);
    }
}
