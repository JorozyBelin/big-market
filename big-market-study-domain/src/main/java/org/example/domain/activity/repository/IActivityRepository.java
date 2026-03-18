package org.example.domain.activity.repository;

import org.example.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.example.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import org.example.domain.activity.model.entity.*;
import org.example.domain.activity.model.vo.ActivitySkuStockKeyVO;

import java.util.Date;

/**
 * 抽奖活动仓库
 */
public interface IActivityRepository {


    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);

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

    /**
     * 查询未使用的订单
     * @param partakeRaffleActivityEntity 参与抽奖活动信息
     * @return 订单信息
     */
    UserRaffleOrderEntity queryNoUserdOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);

    /**
     * 保存聚合对象
     * @param createPartakeOrderAggregate 聚合对象
     */
    void savePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate);

    /**
     * 查询活动账户信息
     * @param userId 用户ID
     * @param activityId 活动ID
     * @return 活动账户信息
     */
    ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId);

    /**
     * 查询活动账户月信息
     * @param userId 用户ID
     * @param activityId 活动ID
     * @param month 月份
     * @return 活动账户月信息
     */
    ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Long activityId, String month);

    /**
     * 查询活动账户日信息
     * @param userId 用户ID
     * @param activityId 活动ID
     * @param day  日
     * @return 活动账户日信息
     */
    ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day);
}
