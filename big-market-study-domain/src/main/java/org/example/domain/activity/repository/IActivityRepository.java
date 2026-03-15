package org.example.domain.activity.repository;

import org.example.domain.activity.model.aggregate.CreateOrderAggregate;
import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.model.vo.ActivitySkuStockKeyVO;

import java.util.Date;

/**
 * 抽奖活动仓库
 */
public interface IActivityRepository {


    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    void doSaveOrder(CreateOrderAggregate createOrderAggregate);

    /**
     * 缓存活动商品库存信息
     *
     * @param sku
     * @param cacheKey   缓存key
     * @param stockCount 库存数量
     */
    void cacheActivitySkuStockCount(Long sku,String cacheKey, Integer stockCount);

    /**
     * 扣减活动商品库存信息
     *
     * @param sku 商品ID
     * @param cacheKey  缓存key
     * @param endDateTime 活动结束时间
     * @return 扣减结果
     */
    boolean subtractionActivitySkuStock(Long sku,String cacheKey, Date endDateTime);
    /**
     * 发送延迟队列
     * @param activitySk 缓存key
     */
    void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO activitySk);

    /**
     * 获取延迟队列
     * @return 活动商品库存信息
     */
    ActivitySkuStockKeyVO takeQueueValue();

    /**
     * 清空延迟队列
     */
    void clearQueueValue();

    /**
     * 更新活动商品库存信息
     * @param sku 商品ID
     */
    void updateActivitySkuStock(Long sku);

    /**
     * 清空活动商品库存信息
     * @param sku 商品ID
     */
    void clearActivitySkuStock(Long sku);
}
