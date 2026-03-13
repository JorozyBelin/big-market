package org.example.domain.activity.service;

import org.example.domain.activity.model.entity.ActivityOrderEntity;
import org.example.domain.activity.model.entity.ActivityShopCartEntity;

/**
 * 抽奖活动订单接口
 */
public interface IRaffleOrder {
    /**
     * 创建抽奖活动订单，以sku创建活动抽奖订单，获得参与抽奖资格
     *
     * @param activityShopCartEntity 活动sku为实体，通过sku领取活动
     * @return 抽奖活动订单
     */
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity);
}
